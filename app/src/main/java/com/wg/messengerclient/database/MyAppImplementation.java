package com.wg.messengerclient.database;

import android.app.Application;
import android.content.Context;

public class MyAppImplementation extends Application {
    private static MyAppImplementation mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        SingletonDatabase.buildDatabase(getApplicationContext());
        mContext = this;
    }

    public static Context getmContext(){
        return mContext;
    }
}
