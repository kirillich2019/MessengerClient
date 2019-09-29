package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import com.wg.messengerclient.activity_and_fargments.ShowingPhoto;
import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.server.Server;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileInfoPresenter extends CacheKeeper {
    private IProfileInfoView view;

    public ProfileInfoPresenter(IProfileInfoView view) {
        this.view = view;

        initialFilling();
    }

    @SuppressLint("CheckResult")
    public void tryGetAndSaveFullProfileInfo() {
        Server.getInstanceShortOperationsServer().serverCheck()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if(result != null) {
                        filingFieldsFromServer();

                        view.hideWarning();
                    }
                }, error -> view.showWarning("Не удаётся получить доступ к серверу."));
    }

    public void initialFilling(){
        filingFieldsFromBD();
        tryGetAndSaveFullProfileInfo();
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
                    
                    if(fullProfileInfoAnswer.getAvatarUrl() != null)
                        view.setUserAvatar(fullProfileInfoAnswer.getAvatarUrl());

                }, error -> view.showError("Не удалось найти данные профиля на локальном хранилище."));
    }

    @SuppressLint("CheckResult")
    private void filingFieldsFromServer() {
        getCurrentUserToken()
                .flatMap(token -> Server.getInstanceShortOperationsServer().getPrivateProfileInfo(token))
                .flatMap(profileInfoAnswer -> saveCurrentUserFullProfileInfo(profileInfoAnswer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profileInfoAnswer -> {
                    if (profileInfoAnswer.getError() != 0) {
                        view.showError(Integer.toString(profileInfoAnswer.getError()));
                        return;
                    }

                    view.setProfileInfo(
                            profileInfoAnswer.getName(),
                            profileInfoAnswer.getSurname(),
                            profileInfoAnswer.getLogin(),
                            profileInfoAnswer.getBirthday(),
                            "fashion bitch"
                    );

                    if(profileInfoAnswer.getAvatar() != null)
                        view.setUserAvatar(profileInfoAnswer.getAvatar());
                }, error -> view.showError("Не удалось подключиться к серверу."));
    }

    @SuppressLint("CheckResult")
    public void showFullSizeProfilePhoto(){
        getCurrentUserFullProfileInfoFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullProfileInfo -> {
                   if(fullProfileInfo.getAvatarUrl() != null){
                       ShowingPhoto.ShowPhoto(view.getAppContext(), fullProfileInfo.getAvatarUrl());
                   }
                });
    }
}
