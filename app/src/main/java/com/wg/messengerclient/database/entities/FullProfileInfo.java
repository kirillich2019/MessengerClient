package com.wg.messengerclient.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FullProfileInfo{
    @PrimaryKey(autoGenerate = true)
    public int index;

    public int id;

    public String login;

    public String name;

    public String surname;

    public String birthday;
}
