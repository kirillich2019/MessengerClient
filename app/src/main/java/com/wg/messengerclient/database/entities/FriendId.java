package com.wg.messengerclient.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FriendId {
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
