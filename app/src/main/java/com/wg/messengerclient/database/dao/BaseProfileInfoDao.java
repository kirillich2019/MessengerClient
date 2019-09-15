package com.wg.messengerclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.wg.messengerclient.database.entities.BaseProfileInfo;

import java.util.List;

@Dao
public abstract class BaseProfileInfoDao {
    /*@Query("SELECT * FROM BaseProfileInfo")
    public abstract List<BaseProfileInfo> getAll();

    @Query("SELECT * FROM BaseProfileInfo WHERE id = :id")
    public abstract BaseProfileInfo getById(int id);

    @Query("SELECT * FROM BaseProfileInfo WHERE token = :token")
    public abstract BaseProfileInfo getByToken(String token);*/

    @Query("SELECT * FROM baseprofileinfo WHERE `index` = 1")
    public abstract BaseProfileInfo getFirstOrNull();

    @Insert
    public abstract void insert(BaseProfileInfo employee);

    @Update
    public abstract void update(BaseProfileInfo employee);

    @Delete
    public abstract void delete(BaseProfileInfo employee);

    @Transaction
    public BaseProfileInfo getOrCreateAndGetCurrentUser() {
        BaseProfileInfo profileInfo = getFirstOrNull();

        if(profileInfo == null) {
            profileInfo = new BaseProfileInfo();
            profileInfo.index = 1;

            insert(profileInfo);
        }

        return profileInfo;
    }
}
