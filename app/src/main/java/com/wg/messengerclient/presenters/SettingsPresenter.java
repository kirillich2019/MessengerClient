package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.mvp_interfaces.ISettingView;
import com.wg.messengerclient.server.Server;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsPresenter extends TokenSaver implements LifecycleObserver {
    private ISettingView settingView;
    private Disposable logoutDisposable, changeProfileInfoDisposable;
    private static final String
            NAME_PARAM_FIELD = "name",
            SURNAME_PARAM_FIELD = "surname",
            BIRTHDAY_PARAM_FIELD = "birthday";
    private FullProfileInfo _currentFullProfileInfo;

    public SettingsPresenter(ISettingView settingView) {
        this.settingView = settingView;
    }

    public void tryChangeProfileInfo(ProfileChangesTransmitter transmitter) {
        if (transmitter.isName_changed() || transmitter.isSurname_changed() || transmitter.isBirthday_changed()) {
            Map<String, String> parametrs = new HashMap<>();

            if (transmitter.isName_changed())
                parametrs.put(NAME_PARAM_FIELD, transmitter.getName());

            if (transmitter.isSurname_changed())
                parametrs.put(SURNAME_PARAM_FIELD, transmitter.getSurname());

            if (transmitter.isBirthday_changed())
                parametrs.put(BIRTHDAY_PARAM_FIELD, transmitter.getBirthday());

            changeProfileInfoDisposable =
                    getCurrentUserToken()
                            .flatMap(token -> Server.getInstance().setProfileFields(token, parametrs))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setProfileInfoAnswer -> {
                                if (setProfileInfoAnswer.getError() != 0) {
                                    settingView.showError(
                                            setProfileInfoAnswer.getError_text() != null ?
                                                    setProfileInfoAnswer.getError_text() :
                                                    Integer.toString(setProfileInfoAnswer.getError())
                                    );

                                    settingView.showError("Изменения успешно сохранены.");

                                    return;
                                }
                            });
        }
    }

    @SuppressLint("CheckResult")
    public void fillFieldsCurrentUserFullProfileInfo(){
        getCurrentUserFullProfileInfoFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullProfileInfo -> {
                    settingView.fillingFieldsCurrentData(fullProfileInfo.name, fullProfileInfo.surname);
                });
    }

    @SuppressLint("CheckResult")
    public void logout() {
        logoutDisposable = delCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delResult -> settingView.openLoginScreen());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        if (logoutDisposable != null)
            logoutDisposable.dispose();

        if (changeProfileInfoDisposable != null)
            changeProfileInfoDisposable.dispose();
    }
}
