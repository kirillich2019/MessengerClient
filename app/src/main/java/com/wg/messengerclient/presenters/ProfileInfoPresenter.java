package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.server.Server;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileInfoPresenter extends TokenSaver{
    private IProfileInfoView view;

    public ProfileInfoPresenter(IProfileInfoView view) {
        this.view = view;

        tryGetFullProfileInfo();
    }

    //todo если 2 ошибка выходить из профиля в регистрацию
    @SuppressLint("CheckResult")
    private void tryGetFullProfileInfo(){
        getCurrentUserToken()
                .flatMap(token -> Server.getInstance().getPrivateProfileInfo(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profileInfoAnswer -> {
                    if(profileInfoAnswer.getError() != 0) {
                        view.showError(Integer.toString(profileInfoAnswer.getError()));
                        return;
                    }

                    view.setProfileInfo(profileInfoAnswer.name, profileInfoAnswer.surname, profileInfoAnswer.login, profileInfoAnswer.birthday, "fashion bitch");
                }, error -> view.showError(error.getMessage()));
    }

    @SuppressLint("CheckResult")
    public void tryLogOut(){
        delCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delResult -> view.openLoginScreen());
    }
}
