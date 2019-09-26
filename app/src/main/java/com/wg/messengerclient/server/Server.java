package com.wg.messengerclient.server;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
    private static final String BASE_URL = "http://176.57.217.44";

    private static MessengerServerApi serverApi;
    private static LongOperationMessengerServerApi longOperationMessengerServerApi;

    public static LongOperationMessengerServerApi getInstanceLongOperationsServer(){
        if(longOperationMessengerServerApi == null) {
            longOperationMessengerServerApi = new Retrofit.Builder()
                    .client(
                            new OkHttpClient.Builder()
                                    .writeTimeout(1, TimeUnit.MINUTES)
                                    .readTimeout(1, TimeUnit.MINUTES)
                                    .build()
                    )
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(LongOperationMessengerServerApi.class);
        }

        return longOperationMessengerServerApi;
    }

    public static MessengerServerApi getInstanceShortOperationsServer(){
        if(serverApi == null) {
            serverApi = new Retrofit.Builder()
                    .client(
                            new OkHttpClient.Builder()
                                    .writeTimeout(20, TimeUnit.SECONDS)
                                    .readTimeout(20, TimeUnit.SECONDS)
                                    .build()
                    )
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(MessengerServerApi.class);
        }

        return serverApi;
    }
}
