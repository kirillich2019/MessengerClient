package com.wg.messengerclient.server;

import com.wg.messengerclient.models.server_answers.AnswerWithError;
import com.wg.messengerclient.models.server_answers.FriendsIDsAnswer;
import com.wg.messengerclient.models.server_answers.LastActionIdAnswer;
import com.wg.messengerclient.models.server_answers.MessagesAnswer;
import com.wg.messengerclient.models.server_answers.ProfileInfoAnswer;
import com.wg.messengerclient.models.server_answers.SendMessageAnswer;
import com.wg.messengerclient.models.server_answers.SetUserInfoAnswer;
import com.wg.messengerclient.models.server_answers.WelcomeAnswer;
import com.wg.messengerclient.models.server_answers.LoginAnswer;
import com.wg.messengerclient.models.server_answers.RegistrationAnswer;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
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

    @GET("/profile/getPublicInfo")
    Observable<ProfileInfoAnswer> getPublicProfileInfo(@Query("id") int id);

    @GET("/profile/setInfo")
    Observable<SetUserInfoAnswer> setProfileFields(@Query("token") String token, @QueryMap Map<String, String> fields);

    @GET("/profile/setInfo")
    Observable<SetUserInfoAnswer> setLoginFields(@Query("token") String token, @Query("current_pass") String currentPass, @QueryMap Map<String, String> fields);

    @GET("/friends/getRequests")
    Observable<FriendsIDsAnswer> getAllFriendsRequests(@Query("token") String token);

    @GET("/friends/answer")
    Observable<AnswerWithError> responseToRequest(@Query("token") String token, @Query("id") int id, @Query("accept") int accept);

    @GET("/friends/sendRequest")
    Observable<AnswerWithError> sendFriendRequest(@Query("token") String token, @Query("login") String login);

    @GET("/friends/getAll")
    Observable<FriendsIDsAnswer> getAllFriends(@Query("token") String token);

    @GET("/dialogs/getMessages")
    Observable<List<MessagesAnswer>> getMessages(@Query("token") String token, @Query("id") int id, @Query("messageId") int messageId, @Query("limit") int limit);

    @GET("/getLastActionId")
    Observable<LastActionIdAnswer> getLastActionId(@Query("token") String token);

    @GET("/dialogs/sendMessage")
    Observable<SendMessageAnswer> sendMessage(@Query("token") String token, @Query("id") int id, @Query("text") String text, @Query("randomUUID") String randomUUID);
}