package com.wg.messengerclient.presenters;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.RandomString;
import com.wg.messengerclient.mvp_interfaces.IDialogView;
import com.wg.messengerclient.presenters.messageSystem.MessageSystemSingleton;
import com.wg.messengerclient.presenters.messageSystem.events.DialogStatusChangeEvent;
import com.wg.messengerclient.presenters.messageSystem.interfaces.MessageSystemEventsListner;
import com.wg.messengerclient.server.MessengerServerApi;
import com.wg.messengerclient.server.Server;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class DialogPresenter implements LifecycleObserver, MessageSystemEventsListner {
    private IDialogView view;

    private CompositeDisposable disposables = new CompositeDisposable();

    private int dialogId;

    private MessengerServerApi server = Server.getInstanceShortOperationsServer();

    private CacheKeeper cacheKeeper = new CacheKeeper();

    private RandomString randomString = new RandomString(10);

    private String currMsgToSend, currRandomUUID;

    public DialogPresenter(IDialogView view, int dialogId) {
        this.view = view;
        this.dialogId = dialogId;
        MessageSystemSingleton.getInstance().addEventListner(this);
    }

    @Override
    public void dialogStatusChange(DialogStatusChangeEvent event) {
        switch (event.getDialogStatus()) {
            case DIALOG_HAVE_CHANGES:
                if (event.getModifiedDialog().dialogDbEntity.getDialogId() == dialogId) {
                    view.showNewMessages(event.getModifiedDialog().dialogDbEntity.getNewMessages());
                }
                break;
        }
    }

    public void trySendMessage(@NonNull String msgText) {
        msgText = msgText.trim();

        if (TextUtils.isEmpty(msgText)) {
            return;
        }

        currMsgToSend = msgText;
        currRandomUUID = randomString.nextString();

        disposables.add(
                cacheKeeper
                        .getCurrentUserToken()
                        .flatMap(token -> server.sendMessage(token, dialogId, currMsgToSend, currRandomUUID))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(messageAnswer -> {
                            if(messageAnswer.getError() != 0)
                                view.showError("Ошибка отправки сообщения");
                            else
                                view.clearNewMsgTextEdit();
                        }, error -> view.showError("Ошибка отправки сообщения"))
        );
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        disposables.dispose();
        MessageSystemSingleton.getInstance().removeEventListner(this);
    }
}
