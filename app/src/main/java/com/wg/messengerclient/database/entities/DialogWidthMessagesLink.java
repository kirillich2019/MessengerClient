package com.wg.messengerclient.database.entities;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;
import java.util.List;

public class DialogWidthMessagesLink {
    @Embedded
    public DialogEntity dialogDbEntity;

    @Relation(parentColumn = "dialogId", entity = MessageDbEntity.class, entityColumn = "dialogId")
    public List<MessageDbEntity> messages;

    public DialogWidthMessagesLink(DialogEntity dialogDbEntity, List<MessageDbEntity> messages) {
        this.dialogDbEntity = dialogDbEntity;
        this.messages = messages;
    }
}
