package com.wg.messengerclient.database;

import android.app.Application;

public class MyAppImplementation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SingletonDatabase.buildDatabase(getApplicationContext());
    }
}
