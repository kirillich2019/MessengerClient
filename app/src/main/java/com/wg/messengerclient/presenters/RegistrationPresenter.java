package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.server.Server;
import com.wg.messengerclient.models.server_answers.Errors;
import com.wg.messengerclient.mvp_interfaces.IRegistrationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegistrationPresenter implements LifecycleObserver {
    private IRegistrationView registrationView;
    private Disposable regDisposable;

    public RegistrationPresenter(IRegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    @SuppressLint("CheckResult")
    public void tryRegistration(@NonNull String login, @NonNull String password, @NonNull String confirmPassword, @NonNull String firstName, @NonNull String lastName){
        login = login.trim();

        if(TextUtils.isEmpty(login)) {
            registrationView.showError("Введите логин.");
            return;
        }

        if(TextUtils.isEmpty(password)) {
            registrationView.showError("Введите пароль.");
            return;
        }

        password = password.trim();
        confirmPassword = confirmPassword.trim();

        if(!password.equals(confirmPassword)) {
            registrationView.showError("Пароли не совпадают.");
            return;
        }

        if(TextUtils.isEmpty(firstName)) {
            registrationView.showError("Введите имя.");
            return;
        }

        if(TextUtils.isEmpty(lastName)) {
            registrationView.showError("Введите фамилию.");
            return;
        }

        firstName = firstName.trim();
        lastName = lastName.trim();

        registrationView.showLoading();

        regDisposable = Server.getInstance().registration(login, password, firstName, lastName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(regAnswer -> {
                    if (regAnswer.getError() == 0) {
                        registrationView.showError(regAnswer.getToken());
                    } else {
                        if(regAnswer.getError_text() != null){
                            registrationView.showError(regAnswer.getError_text());
                        }else {
                            registrationView.showError(Errors.UNKNOWN_ERROR.getMessage());
                        }
                    }

                    registrationView.closeLoading();
                }, error -> {
                    registrationView.showError(Errors.SERVER_ACCESS_ERROR.getMessage());

                    registrationView.closeLoading();
                });
    }

    private void openNextScreen(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onClose(){
        if(regDisposable != null)
            regDisposable.dispose();
    }
}
