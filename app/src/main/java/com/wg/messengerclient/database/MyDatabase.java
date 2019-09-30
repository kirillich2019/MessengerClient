package com.wg.messengerclient.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.dao.FullProfileInfoDao;
import com.wg.messengerclient.database.dao.MyFriendsDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.database.entities.FriendId;
import com.wg.messengerclient.database.entities.FullProfileInfo;

@Database(entities = {BaseProfileInfo.class, FullProfileInfo.class, FriendId.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract BaseProfileInfoDao baseProfileInfoDao();

    public abstract FullProfileInfoDao fullProfileInfoDao();

    public abstract MyFriendsDao myFriendsDao();
}
