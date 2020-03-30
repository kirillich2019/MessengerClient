package com.wg.messengerclient.presenters;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.wg.messengerclient.mvp_interfaces.IChatsView;
import com.wg.messengerclient.presenters.messageSystem.MessageSystemSingleton;
import com.wg.messengerclient.presenters.messageSystem.events.DataLoadingSatusChangeEvent;
import com.wg.messengerclient.presenters.messageSystem.events.DialogStatusChangeEvent;
import com.wg.messengerclient.presenters.messageSystem.interfaces.MessageSystemEventsListner;

/*
Презентор списка диалогов
*/
public class ChatsPresenter implements LifecycleObserver, MessageSystemEventsListner {
    private IChatsView chatsView;

    public ChatsPresenter(IChatsView chatsView) {
        this.chatsView = chatsView;
        initialize();
    }

    private void initialize(){
        MessageSystemSingleton.getInstance().addEventListner(this);
    }

    public void updateDialogs() {
        chatsView.showLoadSpinner(true);
        MessageSystemSingleton.getInstance().startCheckNewMsg();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        MessageSystemSingleton.getInstance().removeEventListner(this);
    }

    @Override
    public void dialogListLoadingStatusChange(DataLoadingSatusChangeEvent event) {
        /*chatsView.showLoadSpinner(false);

        switch (event.getDataLoadingStatus()){
            case COMPLETED:
                chatsView.setChats(MessageSystemSingleton.getInstance().getCurrentDialogs());
                break;
            case FAILED:
                chatsView.showError("chats list loading failed");
                break;
        }*/
    }

    @Override
    public void dialogStatusChange(DialogStatusChangeEvent event) {
        switch (event.getDialogStatus()){
            case DIALOG_UPDATE_COMPLETED:
                chatsView.addOrUpdateChat(event.getModifiedDialog());
                break;
        }
    }
}
