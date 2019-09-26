package com.wg.messengerclient.server;

import com.wg.messengerclient.models.server_answers.ProfileInfoAnswer;
import com.wg.messengerclient.models.server_answers.SetUserInfoAnswer;
import com.wg.messengerclient.models.server_answers.UrlAnswer;
import com.wg.messengerclient.models.server_answers.WelcomeAnswer;
import com.wg.messengerclient.models.server_answers.LoginAnswer;
import com.wg.messengerclient.models.server_answers.RegistrationAnswer;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MessengerServerApi {
    @GET("/login")
    Observable<LoginAnswer> login(@Query("login") String login, @Query("pass") String password);

    @GET("/welcome")
    Observable<WelcomeAnswer> serverCheck();

    @GET("/reg")
    Observable<RegistrationAnswer> registration(@Query("login") String login, @Query("pass") String password, @Query("name") String name, @Query("surname") String surname);

    @GET("/profile/getPrivateInfo")
    Observable<ProfileInfoAnswer> getPrivateProfileInfo(@Query("token") String token);

    @GET("/profile/setInfo")
    Observable<SetUserInfoAnswer> setProfileFields(@Query("token") String token, @QueryMap Map<String, String> fields);

    @GET("/profile/setInfo")
    Observable<SetUserInfoAnswer> setLoginFields(@Query("token") String token, @Query("current_pass") String currentPass, @QueryMap Map<String, String> fields);

    @Multipart
    @POST("/profile/setAvatar")
    Observable<UrlAnswer> setAvatar(@Query("token") String token, @Part MultipartBody.Part icon);
}