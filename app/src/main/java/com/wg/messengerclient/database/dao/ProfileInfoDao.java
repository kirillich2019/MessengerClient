package com.wg.messengerclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wg.messengerclient.database.entities.ProfileInfo;

import java.util.List;

@Dao
public interface ProfileInfoDao {
    @Query("SELECT * FROM profileinfo")
    List<ProfileInfo> getAll();

    @Query("SELECT * FROM profileinfo WHERE id = :id")
    ProfileInfo getById(long id);

    @Insert
    void insert(ProfileInfo employee);

    @Update
    void update(ProfileInfo employee);

    @Delete
    void delete(ProfileInfo employee);
}
