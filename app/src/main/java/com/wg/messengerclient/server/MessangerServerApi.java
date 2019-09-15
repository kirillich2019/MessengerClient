package com.wg.messengerclient.server;

import com.wg.messengerclient.models.server_answers.WelcomeAnswer;
import com.wg.messengerclient.models.server_answers.LoginAnswer;
import com.wg.messengerclient.models.server_answers.RegistrationAnswer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessangerServerApi {
    @GET("/login")
    Observable<LoginAnswer> login(@Query("login") String login, @Query("pass") String password);

    @GET("/welcome")
    Observable<WelcomeAnswer> serverCheck();

    @GET("/reg")
    Observable<RegistrationAnswer> registration(@Query("login") String login, @Query("pass") String password, @Query("name") String name, @Query("surname") String surname);
}