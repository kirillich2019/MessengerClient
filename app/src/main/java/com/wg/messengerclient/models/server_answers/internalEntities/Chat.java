package com.wg.messengerclient.models.server_answers.internalEntities;


import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;

/*
Класс чата для использования на клиенте
 */
public class Chat {
    private String login;

    private String avaUrl;

    private DialogWidthMessagesLink dialog;

    public Chat(String login, String avaUrl, DialogWidthMessagesLink dialog) {
        this.login = login;
        this.avaUrl = avaUrl;
        this.dialog = dialog;
    }

    public String getLogin() {
        return login;
    }

    public String getAvaUrl() {
        return avaUrl;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public DialogWidthMessagesLink getDialog() {
        return dialog;
    }

    public void setDialog(DialogWidthMessagesLink dialog) {
        this.dialog = dialog;
    }
}
