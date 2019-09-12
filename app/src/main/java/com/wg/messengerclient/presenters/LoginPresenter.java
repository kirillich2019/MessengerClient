package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.R;
import com.wg.messengerclient.Server.Server;
import com.wg.messengerclient.mvp_interfaces.ILoginView;

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
        loginView.showError("старт");
    }

    @SuppressLint("CheckResult")
    public void tryLogin(@NonNull String login, @NonNull String password) {
        if (TextUtils.isEmpty(login)) {
            loginView.showError(appContext.getString(R.string.loginFieldIsEmpty));
        } else if (TextUtils.isEmpty(password)) {
            loginView.showError(appContext.getString(R.string.passFieldIsEmpty));
        } else {
            loginView.showLoading();

            Server.getInstance().login(login, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loginAnswer -> {
                        if (loginAnswer.getError() == 0) {
                            loginView.showError(loginAnswer.getToken());
                        } else {
                            loginView.showError(Integer.toString(loginAnswer.getError()));
                        }

                        loginView.closeLoading();
                    }, error -> {
                        loginView.showError("Ошибка доступа к серверу.");

                        loginView.closeLoading();
                    });
        }
    }

    private void openNextSrcreen(){

    }
}