package com.wg.messengerclient.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wg.messengerclient.database.dao.ProfileInfoDao;
import com.wg.messengerclient.database.entities.ProfileInfo;

@Database(entities = {ProfileInfo.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract ProfileInfoDao profileInfoDao();
}
