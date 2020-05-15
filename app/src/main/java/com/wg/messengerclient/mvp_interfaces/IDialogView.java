package com.wg.messengerclient.mvp_interfaces;
import com.wg.messengerclient.database.entities.MessageDbEntity;
import java.util.Collection;

public interface IDialogView extends IShowError {
    void showNewMessages(Collection<MessageDbEntity> newMessages);

    void clearNewMsgTextEdit();
}
