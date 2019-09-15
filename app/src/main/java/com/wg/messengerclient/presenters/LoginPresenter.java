package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.R;
import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.server.Server;
import com.wg.messengerclient.models.server_answers.Errors;
import com.wg.messengerclient.mvp_interfaces.ILoginView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LifecycleObserver {
    private final ILoginView loginView;
    private final Context appContext;

    public LoginPresenter(ILoginView loginView, Context context) {
        this.loginView = loginView;
        appContext = context;
    }

    //todo проверка на существование токена в базе
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onViewLoading() {
    }

    @SuppressLint("CheckResult")
    public void tryLogin(@NonNull String login, @NonNull String password) {
        if (TextUtils.isEmpty(login)) {
            loginView.showError(appContext.getString(R.string.loginFieldIsEmpty));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            loginView.showError(appContext.getString(R.string.passFieldIsEmpty));
            return;
        }

        login = login.trim();
        password = password.trim();

        loginView.showLoading();

        Server.getInstance().login(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginAnswer -> {
                    if (loginAnswer.getError() == 0) {
                        saveToken(loginAnswer.getToken())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {
                                    if(result) {
                                        openNextScreen();
                                    }else {
                                        loginView.showError("Ошибка сохранения токена.");
                                    }

                                    loginView.closeLoading();
                                });
                    } else {
                        loginView.showError(loginAnswer.getError_text() != null ? loginAnswer.getError_text() : Errors.UNKNOWN_ERROR.getMessage());
                        loginView.closeLoading();
                    }
                }, error -> {
                    loginView.showError(error.getMessage());

                    loginView.closeLoading();
                });
    }

    @SuppressLint("CheckResult")
    private Observable<Boolean> saveToken(String token){
        return Observable.fromCallable(() -> {
            try {
                BaseProfileInfoDao baseProfileInfoDao = SingletonDatabase.get_databaseInstance().baseProfileInfoDao();

                BaseProfileInfo currentUser = baseProfileInfoDao.getCurrentUser();
                currentUser.token = token;
                baseProfileInfoDao.update(currentUser);
                return true;
            }catch (Throwable t) {
                return false;
            }
        });
    }

    private void openNextScreen(){
        loginView.showError("tip otkril");
    }
}
