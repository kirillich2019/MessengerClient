package com.wg.messengerclient.presenters.messageSystem.events;

import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;

import java.util.EventObject;

public class DialogStatusChangeEvent extends EventObject {
    private DialogStatus dialogStatus;

    private DialogWidthMessagesLink modifiedDialog;

    public DialogStatusChangeEvent(Object source, DialogStatus dialogStatus, DialogWidthMessagesLink modifiedDialog) {
        super(source);
        this.dialogStatus = dialogStatus;
        this.modifiedDialog = modifiedDialog;
    }

    public DialogStatusChangeEvent(Object source, DialogStatus dialogStatus) {
        super(source);
        this.dialogStatus = dialogStatus;
    }

    public DialogStatus getDialogStatus() {
        return dialogStatus;
    }

    public DialogWidthMessagesLink getModifiedDialog() {
        return modifiedDialog;
    }
}
