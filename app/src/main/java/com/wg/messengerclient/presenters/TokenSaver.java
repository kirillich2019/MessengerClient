package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.dao.FullProfileInfoDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.models.server_answers.ProfileInfoAnswer;

import io.reactivex.Observable;

public abstract class TokenSaver {
    BaseProfileInfoDao baseProfileInfoDao = SingletonDatabase.getDatabaseInstance().baseProfileInfoDao();
    FullProfileInfoDao fullProfileInfoDao = SingletonDatabase.getDatabaseInstance().fullProfileInfoDao();



    @SuppressLint("CheckResult")
    public Observable saveToken(String token) {
        return Observable.fromCallable(() -> {
                BaseProfileInfo currentUser = baseProfileInfoDao.getOrCreateAndGetCurrentUser();
                currentUser.token = token;
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


    public Observable<String> getCurrentUserToken(){
        return Observable.fromCallable(() -> baseProfileInfoDao.getFirstOrNull().token);
    }

    public Observable<Boolean> delCurrentUser(){
        return Observable.fromCallable(() -> {
            BaseProfileInfo userInDb = baseProfileInfoDao.getFirstOrNull();
            FullProfileInfo fullProfileInfoInDb = fullProfileInfoDao.getFirstOrNull();

            if(userInDb != null)
                baseProfileInfoDao.delete(userInDb);

            if(fullProfileInfoInDb != null)
                fullProfileInfoDao.delete(fullProfileInfoInDb);

            return true;
        });
    }

    public Observable<ProfileInfoAnswer> saveCurrentUserFullProfileInfo(ProfileInfoAnswer answer){
        return Observable.fromCallable(() -> {
            if(answer.getError() == 0) {
                FullProfileInfo currentUser = fullProfileInfoDao.getOrCreateAndGetCurrentUser();

                currentUser.id = answer.id;
                currentUser.login = answer.login;
                currentUser.name = answer.name;
                currentUser.surname = answer.surname;
                currentUser.birthday = answer.birthday;

                fullProfileInfoDao.update(currentUser);
            }

            return answer;
        });
    }

    protected Observable<FullProfileInfo> getCurrentUserFullProfileInfoFromDB(){
        return Observable.fromCallable(() -> fullProfileInfoDao.getFirstOrNull());
    }
}
