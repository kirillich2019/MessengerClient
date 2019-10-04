package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.dao.FullProfileInfoDao;
import com.wg.messengerclient.database.dao.MyFriendsDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.database.entities.FriendId;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.models.server_answers.ProfileInfoAnswer;
import com.wg.messengerclient.models.server_answers.UrlAnswer;

import java.util.List;

import io.reactivex.Observable;

public class CacheKeeper {
    BaseProfileInfoDao baseProfileInfoDao = SingletonDatabase.getDatabaseInstance().baseProfileInfoDao();
    FullProfileInfoDao fullProfileInfoDao = SingletonDatabase.getDatabaseInstance().fullProfileInfoDao();
    MyFriendsDao myFriendsDao = SingletonDatabase.getDatabaseInstance().myFriendsDao();

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

    protected Observable<FullProfileInfo> getCurrentUserFullProfileInfoFromDB() {
        return Observable.fromCallable(() -> fullProfileInfoDao.getFirstOrNull());
    }

    protected Observable<ProfileInfoAnswer> cacheFriendUser(ProfileInfoAnswer profileInfoAnswer) {
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

    protected Observable<List<FriendId>> getCacheFrieds(){
        return Observable.fromCallable(() -> myFriendsDao.getAll());
    }

    protected Observable<FullProfileInfo> getFullProfileInfoById(int id){
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
}
