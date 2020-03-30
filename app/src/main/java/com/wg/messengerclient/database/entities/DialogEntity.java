package com.wg.messengerclient.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity

public class DialogEntity {
    //ключ диалога так-же id собеседника
    @PrimaryKey(autoGenerate = false)
    private int dialogId;

    @Ignore
    private FullProfileInfo interlocutor;

    @Ignore
    private boolean thereAreNewMsg;

    public DialogEntity(int dialogId){
        this.dialogId = dialogId;
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public boolean getThereAreNewMsg() {
        return thereAreNewMsg;
    }

    public void setThereAreNewMsg(boolean thereAreNewMsg) {
        this.thereAreNewMsg = thereAreNewMsg;
    }

    public FullProfileInfo getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(FullProfileInfo interlocutor) {
        this.interlocutor = interlocutor;
    }
}
