package com.wg.messengerclient.server;

import com.wg.messengerclient.models.server_answers.UrlAnswer;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LongOperationMessengerServerApi {
    @Multipart
    @POST("/profile/setAvatar")
    Observable<UrlAnswer> setAvatar(@Query("token") String token, @Part MultipartBody.Part icon);
}
