package com.wg.messengerclient.database;

import android.content.Context;

import androidx.room.Room;

public class SingletonDatabase {
    private static MyDatabase databaseInstance;

    public static void buildDatabase(Context appContext){
        databaseInstance = Room.databaseBuilder(appContext, MyDatabase.class, "cacheProfileInfo").build();
    }

    public static MyDatabase getDatabaseInstance(){
        if(databaseInstance == null)
            throw new NullPointerException("Ссылка на базу данных не созданна. Для начала вызовите метод buildDatabase.");

        return databaseInstance;
    }
}
