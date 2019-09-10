package com.wg.messengerclient;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyServer {

    String BASE_URL = "http://176.57.217.44";

    static MyServer getInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyServer.class);
    }


    @GET("/login")
    Observable<LoginAnswer> login(@Query("login") String login, @Query("pass") String password);

    @GET("/welcome")
    Observable<WelcomeAnswer> serverCheck();
}