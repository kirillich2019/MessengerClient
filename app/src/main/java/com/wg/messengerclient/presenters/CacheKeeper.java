package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wg.messengerclient.database.MyAppImplementation;
import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.dao.DialogDao;
import com.wg.messengerclient.database.dao.FullProfileInfoDao;
import com.wg.messengerclient.database.dao.MyFriendsDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.database.entities.DialogEntity;
import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.database.entities.FriendId;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.models.server_answers.ProfileInfoAnswer;
import com.wg.messengerclient.models.server_answers.UrlAnswer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CacheKeeper {
    BaseProfileInfoDao baseProfileInfoDao = SingletonDatabase.getDatabaseInstance().baseProfileInfoDao();
    FullProfileInfoDao fullProfileInfoDao = SingletonDatabase.getDatabaseInstance().fullProfileInfoDao();
    DialogDao dialogDao = SingletonDatabase.getDatabaseInstance().dialogsDao();
    MyFriendsDao myFriendsDao = SingletonDatabase.getDatabaseInstance().myFriendsDao();

    private SharedPreferences sPref;
    private final String LAST_ACTION = "LAST_ACTION_ID";
    private final int DEFAULT_ACTION_ID = -1;

    public CacheKeeper(){
        sPref = PreferenceManager.getDefaultSharedPreferences(MyAppImplementation.getmContext());
    }

    @SuppressLint("CheckResult")
    public Observable saveToken(String token) {
        return Observable.fromCallable(() -> {
            BaseProfileInfo currentUser = baseProfileInfoDao.getOrCreateAndGetCurrentUser();
            currentUser.setToken(token);
            baseProfileInfoDao.update(currentUser);
            return true;
        });
    }

    public Observable<Boolean> checkTokenInDatabase() {
        return Observable.fromCallable(() -> {
            BaseProfileInfo userInDb = baseProfileInfoDao.getFirstOrNull();

            return userInDb != null;
        });
    }


    public Observable<String> getCurrentUserToken() {
        return Observable.fromCallable(() -> baseProfileInfoDao.getFirstOrNull().getToken());
    }

    public Observable<BaseProfileInfo> getCurrentUser(){
        return Observable.fromCallable(() -> baseProfileInfoDao.getFirstOrNull());
    }

    public Observable<Boolean> delCurrentUser() {
        return Observable.fromCallable(() -> {
            BaseProfileInfo userInDb = baseProfileInfoDao.getFirstOrNull();
            FullProfileInfo fullProfileInfoInDb = fullProfileInfoDao.getFirstOrNull();

            if (userInDb != null)
                baseProfileInfoDao.delete(userInDb);

            if (fullProfileInfoInDb != null)
                fullProfileInfoDao.delete(fullProfileInfoInDb);

            return true;
        });
    }

    public Observable<ProfileInfoAnswer> saveCurrentUserFullProfileInfo(ProfileInfoAnswer answer) {
        return Observable.fromCallable(() -> {
            if (answer.getError() == 0) {
                FullProfileInfo currentUser = fullProfileInfoDao.getOrCreateAndGetCurrentUser();

                currentUser.setId(answer.getId());
                currentUser.setLogin(answer.getLogin());
                currentUser.setName(answer.getName());
                currentUser.setSurname(answer.getSurname());
                currentUser.setBirthday(answer.getBirthday());
                currentUser.setAvatarUrl(answer.getAvatar());

                fullProfileInfoDao.update(currentUser);
            }

            return answer;
        });
    }

    public Observable<UrlAnswer> saveCurrentUserAvatarUrl(UrlAnswer answer) {
        return Observable.fromCallable(() -> {
            if (answer.getError() == 0) {
                FullProfileInfo currentUser = fullProfileInfoDao.getFirstOrNull();

                currentUser.setAvatarUrl(answer.getUrl());
                fullProfileInfoDao.update(currentUser);
            }

            return answer;
        });
    }

    public Observable<FullProfileInfo> getCurrentUserFullProfileInfoFromDB() {
        return Observable.fromCallable(() -> fullProfileInfoDao.getFirstOrNull());
    }

    public Observable<ProfileInfoAnswer> cacheFriendUser(ProfileInfoAnswer profileInfoAnswer) {
        return Observable.fromCallable(() -> {
            if (profileInfoAnswer.getError() == 0) {
                FriendId friendId = myFriendsDao.getById(profileInfoAnswer.getId());

                if (friendId == null) {
                    FriendId newFriendsId = new FriendId();
                    newFriendsId.setId(profileInfoAnswer.getId());
                    myFriendsDao.insert(newFriendsId);
                }

                cacheUser(profileInfoAnswer);
            }

            return profileInfoAnswer;
        });
    }

    public Observable<List<FriendId>> getCacheFrieds(){
        return Observable.fromCallable(() -> myFriendsDao.getAll());
    }

    public Observable<FullProfileInfo> getFullProfileInfoById(int id){
        return Observable.fromCallable(() -> {
            FullProfileInfo fullProfileInfo = fullProfileInfoDao.getById(id);

            if (fullProfileInfo == null)
                return new FullProfileInfo();

            return fullProfileInfo;
        });
    }

    private void cacheUser(ProfileInfoAnswer profileInfoAnswer) {
        FullProfileInfo profileInfo = fullProfileInfoDao.getById(profileInfoAnswer.getId());

        boolean insert = false;

        if (profileInfo == null) {
            profileInfo = new FullProfileInfo();
            insert = true;
        }

        profileInfo.setAvatarUrl(profileInfoAnswer.getAvatar());
        profileInfo.setBirthday(profileInfoAnswer.getBirthday());
        profileInfo.setLogin(profileInfoAnswer.getLogin());
        profileInfo.setName(profileInfoAnswer.getName());
        profileInfo.setSurname(profileInfoAnswer.getSurname());
        profileInfo.setId(profileInfoAnswer.getId());

        if (insert)
            fullProfileInfoDao.insert(profileInfo);
        else
            fullProfileInfoDao.update(profileInfo);
    }

    /*public Observable<Boolean> saveLastActionsId(int lastActionsId){
        return Observable.fromCallable(() -> {
            BaseProfileInfo currentUser = baseProfileInfoDao.getFirstOrNull();

            if(currentUser == null)
                return false;

            currentUser.setLastActionsId(lastActionsId);
            baseProfileInfoDao.update(currentUser);

            return true;
        });
    }*/

    public Observable<List<DialogWidthMessagesLink>> getAllDialogs(){
        return Observable.fromCallable(() -> dialogDao.getAll());
    }

    public Observable<Boolean> addDialogToDB(int dialogId){
        return Observable.fromCallable(() -> {
           DialogWidthMessagesLink dialog = dialogDao.getDialogById(dialogId);

           if(dialog != null)
               return true;

           DialogEntity dialogDbEntity = new DialogEntity(dialogId);
           dialog = new DialogWidthMessagesLink(dialogDbEntity,null);

           dialogDao.insert(dialog);

           return true;
        });
    }

    public Observable<DialogWidthMessagesLink> getDialogById(int dialogId){
        return Observable.fromCallable(() -> {
            DialogWidthMessagesLink dialog = dialogDao.getDialogById(dialogId);

            if(dialog == null)
                return new DialogWidthMessagesLink(null, null);

            return dialog;
        });
    }

    public Observable<Boolean> saveLastActionId(int lastActionId){
        return Observable.fromCallable(() ->
        {
            Integer id = baseProfileInfoDao.getFirstOrNull().getId();

            if(id == null)
                return false;

            sPref.edit().putInt(LAST_ACTION + id.toString(), lastActionId);
            sPref.edit().commit();

            return true;

            /*BaseProfileInfo currentUser = baseProfileInfoDao.getFirstOrNull();
            if(currentUser == null) {
                return false;
            }

            currentUser.setLastActionsId(lastActionId);

            baseProfileInfoDao.update(currentUser);

            return true;*/
        });
    }

    public Observable<Integer> getLastActionId(){
        return Observable.fromCallable(() ->
        {
            Integer id = baseProfileInfoDao.getFirstOrNull().getId();

            if(id == null)
                return DEFAULT_ACTION_ID;

            Integer result = sPref.getInt(LAST_ACTION + id.toString(), DEFAULT_ACTION_ID);

            return result;

            /*BaseProfileInfo currentUser = baseProfileInfoDao.getFirstOrNull();

            if(currentUser == null)
                return -1;

            return currentUser.getLastActionsId();*/
        });
    }
}
