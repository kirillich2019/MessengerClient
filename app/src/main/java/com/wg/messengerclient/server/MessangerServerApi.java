package com.wg.messengerclient.server;

import com.wg.messengerclient.models.server_answers.ProfileInfoAnswer;
import com.wg.messengerclient.models.server_answers.SetInfoAnswer;
import com.wg.messengerclient.models.server_answers.WelcomeAnswer;
import com.wg.messengerclient.models.server_answers.LoginAnswer;
import com.wg.messengerclient.models.server_answers.RegistrationAnswer;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MessangerServerApi {
    @GET("/login")
    Observable<LoginAnswer> login(@Query("login") String login, @Query("pass") String password);

    @GET("/welcome")
    Observable<WelcomeAnswer> serverCheck();

    @GET("/reg")
    Observable<RegistrationAnswer> registration(@Query("login") String login, @Query("pass") String password, @Query("name") String name, @Query("surname") String surname);

    @GET("/profile/getPrivateInfo")
    Observable<ProfileInfoAnswer> getPrivateProfileInfo(@Query("token") String token);

    @GET("/profile/setInfo")
    Observable<SetInfoAnswer> setProfileFields(@Query("token") String token, @QueryMap Map<String, String> fields);
}