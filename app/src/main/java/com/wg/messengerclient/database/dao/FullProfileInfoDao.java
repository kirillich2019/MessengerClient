package com.wg.messengerclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.wg.messengerclient.database.entities.FullProfileInfo;

@Dao
public abstract class FullProfileInfoDao {
    @Query("SELECT * FROM fullprofileinfo WHERE `index` = 1")
    public abstract FullProfileInfo getFirstOrNull();

    @Query("SELECT * FROM fullprofileinfo WHERE `id` = :userId")
    public abstract FullProfileInfo getById(int userId);

    @Insert
    public abstract void insert(FullProfileInfo employee);

    @Update
    public abstract void update(FullProfileInfo employee);

    @Delete
    public abstract void delete(FullProfileInfo employee);

    @Transaction
    public FullProfileInfo getOrCreateAndGetCurrentUser() {
        FullProfileInfo profileInfo = getFirstOrNull();

        if(profileInfo == null) {
            profileInfo = new FullProfileInfo();
            profileInfo.setIndex(1);

            insert(profileInfo);
        }

        return profileInfo;
    }
}
