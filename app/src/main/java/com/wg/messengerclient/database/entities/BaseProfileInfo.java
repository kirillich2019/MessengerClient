package com.wg.messengerclient.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BaseProfileInfo {
    @PrimaryKey(autoGenerate = true)
    public int index;

    public int id;

    public String token;
}
