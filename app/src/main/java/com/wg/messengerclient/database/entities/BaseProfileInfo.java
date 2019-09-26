package com.wg.messengerclient.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BaseProfileInfo {
    @PrimaryKey(autoGenerate = true)
    private int index;

    private int id;

    private String token;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
