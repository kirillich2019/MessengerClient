package com.wg.messengerclient.presenters.messageSystem;

import android.annotation.SuppressLint;
import com.wg.messengerclient.Utils;
import com.wg.messengerclient.database.entities.DialogEntity;
import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.database.entities.MessageDbEntity;
import com.wg.messengerclient.models.server_answers.MessagesAnswer;
import com.wg.messengerclient.presenters.CacheKeeper;
import com.wg.messengerclient.presenters.messageSystem.events.DialogStatus;
import com.wg.messengerclient.presenters.messageSystem.events.DialogStatusChangeEvent;
import com.wg.messengerclient.presenters.messageSystem.interfaces.MessageSystemEventsListner;
import com.wg.messengerclient.server.MessengerServerApi;
import com.wg.messengerclient.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MessageSystemSingleton {
    private static MessageSystemSingleton instance;

    public static String DIALOG_ID_EXTRA_TAG = "DIALOG_ID_EXTRA_TAG";

    private CompositeDisposable disposables = new CompositeDisposable();

    private ArrayList<MessageSystemEventsListner> listeners = new ArrayList<MessageSystemEventsListner>();

    private CacheKeeper cacheKeeper = new CacheKeeper();

    private HashMap<Integer, DialogWidthMessagesLink> currentDialogs = new HashMap<>();

    MessengerServerApi server = Server.getInstanceShortOperationsServer();

    private boolean updateInProgress = false;

    @SuppressLint("CheckResult")
    public MessageSystemSingleton() {

    }

    public static MessageSystemSingleton getInstance() {
        if (instance == null) instance = new MessageSystemSingleton();
        return instance;
    }

    public HashMap<Integer, DialogWidthMessagesLink> getCurrentDialogs() {
        return currentDialogs;
    }

    /*
    Система событий
     */
    public void addEventListner(MessageSystemEventsListner messageSystemEventsListner) {
        listeners.add(messageSystemEventsListner);
    }

    public void removeEventListner(MessageSystemEventsListner messageSystemEventsListner) {
        listeners.remove(messageSystemEventsListner);
    }

    //Вызов события изменения статуса диалога
    protected void fireDialogStatusChangeEvent(DialogStatusChangeEvent dialogStatusChangeEvent) {
        for (MessageSystemEventsListner listner : listeners) {
            listner.dialogStatusChange(dialogStatusChangeEvent);
        }
    }

    public void startCheckNewMsg() {

        if(!updateInProgress){
            disposables.add(
                    Observable
                            .interval(1, TimeUnit.SECONDS)
                            .flatMap(a -> cacheKeeper.getCurrentUserToken())
                            .flatMap(token -> server.getAllFriends(token))
                            .flatMap(friendIds -> Observable.fromIterable(friendIds.getIds()))
                            .flatMap(friendId -> downloadDialog(friendId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(dialog -> {
                                if (dialog.dialogDbEntity.isThereAreNewMsg())
                                    fireDialogStatusChangeEvent(new DialogStatusChangeEvent(this, DialogStatus.DIALOG_HAVE_CHANGES, dialog));
                                else
                                    fireDialogStatusChangeEvent(new DialogStatusChangeEvent(this, DialogStatus.DIALOG_DONT_HAVE_CHANGES, dialog));

                                updateInProgress = true;
                            }, error -> {
                                fireDialogStatusChangeEvent(new DialogStatusChangeEvent(this, DialogStatus.DIALOG_LOADING_FILED));

                                updateInProgress = false;
                            })
            );
        }
    }

    private Observable<DialogWidthMessagesLink> downloadDialog(int friendId) {
        return Observable.fromCallable(() -> {
            int lastMsgId = 0;

            DialogWidthMessagesLink dialog = null;

            Boolean dialogFound = currentDialogs.containsKey(friendId);

            if (dialogFound) {
                dialog = currentDialogs.get(friendId);
                if (dialog.messages.size() > 0) {
                    lastMsgId = dialog.messages.get(dialog.messages.size() - 1).getMessage().getId();
                }
            }

            FullProfileInfo friendProfileInfo = Utils.await(cacheKeeper.getFullProfileInfoById(friendId));
            String token = Utils.await(cacheKeeper.getCurrentUserToken());

            ArrayList<MessageDbEntity> newMessages = new ArrayList<>();

            int limit = 1000;

            while (true) {
                List<MessagesAnswer> newMessagesAnswer = Utils.await(
                        Server.getInstanceShortOperationsServer().getMessages(token, friendId, lastMsgId + 1, limit));

                for (MessagesAnswer msg :
                        newMessagesAnswer) {
                    newMessages.add(new MessageDbEntity(friendId, msg));
                }

                if (newMessages.size() < 1000)
                    break;

                lastMsgId += limit;
            }

            Boolean newMsgExist = newMessages.size() > 0;

            if (!dialogFound) {
                DialogEntity dialogEntity = new DialogEntity(friendId);
                dialogEntity.setInterlocutor(friendProfileInfo);

                dialog = new DialogWidthMessagesLink(dialogEntity, newMessages);

                if(newMsgExist)
                    dialog.dialogDbEntity.setNewMessages(newMessages);

                dialog.dialogDbEntity.setThereAreNewMsg(true);
                currentDialogs.put(dialog.dialogDbEntity.getDialogId(), dialog);
            } else {
                dialog.messages.addAll(newMessages);

                if(newMsgExist)
                    dialog.dialogDbEntity.setNewMessages(newMessages);

                dialog.dialogDbEntity.setThereAreNewMsg(newMsgExist);
            }

            return dialog;
        });
    }

    public DialogWidthMessagesLink getSpecificDialog(int dialogId) {
        if (currentDialogs.containsKey(dialogId))
            return currentDialogs.get(dialogId);

        return null;
    }

    public void stopCheckNewMsg() {
        disposables.dispose();
    }
}
