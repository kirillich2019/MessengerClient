package com.wg.messengerclient.mvp_interfaces;

import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.models.server_answers.internalEntities.Chat;

import java.util.Collection;

public interface IChatsView extends IShowError {
    void onFragmentShow();

    void showLoadSpinner(boolean show);

    void setChats(Collection<DialogWidthMessagesLink> chatsCollection);

    void addOrUpdateChat(DialogWidthMessagesLink chat);

    void addChat(DialogWidthMessagesLink chat);

    void clearChats();
}
