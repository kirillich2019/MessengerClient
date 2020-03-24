package com.wg.messengerclient.database.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

 import com.wg.messengerclient.models.server_answers.MessagesAnswer;

/*
сообщение для хранения в бд
 */
@Entity(
        foreignKeys = @ForeignKey(
                entity = DialogEntity.class,
                parentColumns = "dialogId",
                childColumns = "dialogId",
                onDelete = ForeignKey.CASCADE))

public class MessageDbEntity {
    @PrimaryKey(autoGenerate = true)
    int index;

    @Embedded
    MessagesAnswer message;

    private int dialogId;

    public MessageDbEntity(int dialogId, MessagesAnswer message) {
        this.message = message;
        this.dialogId = dialogId;
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public MessagesAnswer getMessage() {
        return message;
    }

    public void setMessage(MessagesAnswer message) {
        this.message = message;
    }
}
