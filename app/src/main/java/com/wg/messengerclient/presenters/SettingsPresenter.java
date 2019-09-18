package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.mvp_interfaces.ISettingView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsPresenter extends TokenSaver implements LifecycleObserver {
    private ISettingView settingView;
    private Disposable logoutDisposable;

    public SettingsPresenter(ISettingView settingView) {
        this.settingView = settingView;
    }

    @SuppressLint("CheckResult")
    public void logout(){
        logoutDisposable = delCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delResult -> settingView.openLoginScreen());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        if (logoutDisposable != null)
            logoutDisposable.dispose();
    }
}
