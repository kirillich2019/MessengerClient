package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.server.Server;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileInfoPresenter extends TokenSaver {
    private IProfileInfoView view;

    public ProfileInfoPresenter(IProfileInfoView view) {
        this.view = view;

        tryGetAndSaveFullProfileInfo();
    }

    //todo кешированные данные грузить после проверки сети
    @SuppressLint("CheckResult")
    public void tryGetAndSaveFullProfileInfo() {
        Server.getInstance().serverCheck()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result == null) {
                        filingFieldsFromBD();

                        view.showWarning("Не удаётся получить доступ к серверу.");
                    } else {
                        filingFieldsFromServer();

                        view.hideWarning();
                    }
                }, error -> {
                    filingFieldsFromBD();
                    view.showWarning("Не удаётся получить доступ к серверу.");
                });
    }

    @SuppressLint("CheckResult")
    private void filingFieldsFromBD() {
        getCurrentUserFullProfileInfoFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullProfileInfoAnswer -> {
                    view.setProfileInfo(
                            fullProfileInfoAnswer.getName(),
                            fullProfileInfoAnswer.getSurname(),
                            fullProfileInfoAnswer.getLogin(),
                            fullProfileInfoAnswer.getBirthday(),
                            "fashion bitch"
                    );
                }, error -> view.showError("Не удалось найти данные профиля на локальном хранилище."));
    }

    @SuppressLint("CheckResult")
    private void filingFieldsFromServer() {
        getCurrentUserToken()
                .flatMap(token -> Server.getInstance().getPrivateProfileInfo(token))
                .flatMap(profileInfoAnswer -> saveCurrentUserFullProfileInfo(profileInfoAnswer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profileInfoAnswer -> {
                    if (profileInfoAnswer.getError() != 0) {
                        view.showError(Integer.toString(profileInfoAnswer.getError()));
                        return;
                    }

                    view.setProfileInfo(
                            profileInfoAnswer.name,
                            profileInfoAnswer.surname,
                            profileInfoAnswer.login,
                            profileInfoAnswer.birthday,
                            "fashion bitch"
                    );
                }, error -> view.showError("Не удалось подключиться к серверу."));
    }
}
