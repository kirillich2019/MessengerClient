package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.Server.Server;
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
    public void tryRegistration(@NonNull String login, @NonNull String password, @NonNull String firstName, @NonNull String lastName){
        if(TextUtils.isEmpty(login)) {
            registrationView.showError("Введите логин.");
        }else if(TextUtils.isEmpty(password)) {
            registrationView.showError("Введите пароль.");
        }else if(TextUtils.isEmpty(firstName)) {
            registrationView.showError("Введите имя.");
        }else if(TextUtils.isEmpty(lastName)) {
            registrationView.showError("Введите фамилию.");
        }else {
            registrationView.showLoading();

            regDisposable = Server.getInstance().registration(login, password, firstName, lastName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(regAnswer -> {
                        if (regAnswer.getError() == 0) {
                            registrationView.showError(regAnswer.getToken());
                        } else {
                            Errors error = Errors.getErrorByCode(regAnswer.getError());
                            if(error != null)
                            {
                                if(error.isMessageInClient()) {
                                    registrationView.showError(error.getMessage());
                                }
                                else {
                                    registrationView.showError(regAnswer.getError_text());
                                }
                            }
                        }

                        registrationView.closeLoading();
                    }, error -> {
                        registrationView.showError("Ошибка доступа к серверу.");

                        registrationView.closeLoading();
                    });
        }
    }

    private void openNextScreen(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onClose(){
        if(regDisposable != null)
            regDisposable.dispose();
    }
}
