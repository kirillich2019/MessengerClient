package com.wg.messengerclient.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.wg.messengerclient.database.entities.DialogEntity;
import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.database.entities.MessageDbEntity;

import java.util.List;

@Dao
public abstract class DialogDao {
    @Transaction
    public void insert(DialogWidthMessagesLink dialogWidthMessagesDbEntity) {
        insert(dialogWidthMessagesDbEntity.dialogDbEntity);

        if(dialogWidthMessagesDbEntity.messages == null)
            return;

        for(MessageDbEntity msg: dialogWidthMessagesDbEntity.messages) {
            insert(msg);
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(DialogEntity dialog);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(MessageDbEntity msg);

    @Transaction @Query("SELECT * FROM DialogEntity WHERE dialogId = :id")
    public abstract DialogWidthMessagesLink getDialogById(int id);

    @Query("SELECT * FROM DialogEntity")
    public abstract List<DialogWidthMessagesLink> getAll();
}
