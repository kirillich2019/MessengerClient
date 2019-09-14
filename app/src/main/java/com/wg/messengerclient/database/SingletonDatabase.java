package com.wg.messengerclient.database;

import android.content.Context;

import androidx.room.Room;

public class SingletonDatabase {
    private static MyDatabase _databaseInstance;

    public static void BuildDatabase(Context appContext){
        _databaseInstance = Room.databaseBuilder(appContext, MyDatabase.class, "cacheProfileInfo").build();
    }

    public static MyDatabase get_databaseInstance(){
        if(_databaseInstance == null)
            throw new NullPointerException("Ссылка на базу данных не созданна. Для начала вызовите метод BuildDatabase.");

        return _databaseInstance;
    }
}
