package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;

import io.reactivex.Observable;

public abstract class TokenSaver {
    BaseProfileInfoDao baseProfileInfoDao = SingletonDatabase.get_databaseInstance().baseProfileInfoDao();

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

            return userInDb == null ? false : true;
        });
    }


    public Observable<String> getCurrentUserToken(){
        return Observable.fromCallable(() -> baseProfileInfoDao.getFirstOrNull().token);
    }
}
