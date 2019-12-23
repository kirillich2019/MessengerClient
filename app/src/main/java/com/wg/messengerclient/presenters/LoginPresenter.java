package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.R;
import com.wg.messengerclient.server.Server;
import com.wg.messengerclient.models.server_answers.Errors;
import com.wg.messengerclient.mvp_interfaces.ILoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends CacheKeeper implements LifecycleObserver {
    private final ILoginView loginView;
    private final Context appContext;
    private Disposable loginDisposable;

    public LoginPresenter(ILoginView loginView, Context context) {
        this.loginView = loginView;
        appContext = context;
    }

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onViewLoading() {
        checkTokenInDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                   if(result)
                       loginView.openNextScreen();
                });
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

        loginDisposable = Server.getInstanceShortOperationsServer().login(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginAnswer -> {
                    if (loginAnswer.getError() == 0) {
                        saveToken(loginAnswer.getToken())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {
                                    loginView.closeLoading();
                                    loginView.openNextScreen();
                                });
                    } else {
                        loginView.showError(loginAnswer.getErrorText() != null ? loginAnswer.getErrorText() : Errors.UNKNOWN_ERROR.getMessage());
                        loginView.closeLoading();
                    }
                }, error -> {
                    //loginView.showError("Нет подключения к интернету.");
                    loginView.showError(error.getMessage());
                    loginView.closeLoading();
                });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onClose(){
        if(loginDisposable != null) {
            loginDisposable.dispose();
            loginView.closeLoading();
        }
    }
}
