package com.wg.messengerclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wg.messengerclient.database.entities.FriendId;

import java.util.List;

@Dao
public abstract class MyFriendsDao {
    @Query("SELECT * FROM friendid")
    public abstract List<FriendId> getAll();

    @Query("SELECT * FROM friendid WHERE `id` = :userId")
    public abstract FriendId getById(int userId);

    @Insert
    public abstract void insert(FriendId employee);

    @Update
    public abstract void update(FriendId employee);

    @Delete
    public abstract void delete(FriendId employee);
}
