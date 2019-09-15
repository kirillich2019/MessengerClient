package com.wg.messengerclient.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;

@Database(entities = {BaseProfileInfo.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract BaseProfileInfoDao baseProfileInfoDao();
}
