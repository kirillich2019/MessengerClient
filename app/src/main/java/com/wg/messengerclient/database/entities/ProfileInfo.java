package com.wg.messengerclient.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProfileInfo {
    @PrimaryKey
    public int id;

    public String token;
}
