package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.mvp_interfaces.ISettingView;
import com.wg.messengerclient.presenters.transmitters.LoginInfoChangeTransmitter;
import com.wg.messengerclient.presenters.transmitters.ProfileInfoChangesTransmitter;
import com.wg.messengerclient.server.Server;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingsPresenter extends TokenSaver implements LifecycleObserver {
    private ISettingView settingView;
    private FragmentManager fragmentManager;
    private CompositeDisposable disposables = new CompositeDisposable();
    private static final String
            NAME_PARAM_FIELD = "name",
            SURNAME_PARAM_FIELD = "surname",
            BIRTHDAY_PARAM_FIELD = "birthday",
            NEW_PASS_FILED = "pass",
            NEW_LOGIN_FIELD = "login";

    public SettingsPresenter(ISettingView settingView) {
        this.settingView = settingView;
    }

    public void tryChangeProfileInfo(ProfileInfoChangesTransmitter transmitter) {
        settingView.cleanAllErrors();

        if (transmitter.isNameChanged() || transmitter.isSurnameChanged() || transmitter.isBirthday_changed()) {
            if (TextUtils.isEmpty(transmitter.getName())) {
                settingView.setChangeNameError("Поле имени не может быть пусто.");
                return;
            }

            if (TextUtils.isEmpty(transmitter.getSurname())) {
                settingView.setChangeSurnameError("Поле фамилии не может быть пусто.");
                return;
            }

            Map<String, String> parameters = new LinkedHashMap<>();

            if (transmitter.isNameChanged()) {
                parameters.put(NAME_PARAM_FIELD, transmitter.getName());
            }

            if (transmitter.isSurnameChanged()) {
                parameters.put(SURNAME_PARAM_FIELD, transmitter.getSurname());
            }

            if (transmitter.isBirthday_changed()) {
                parameters.put(BIRTHDAY_PARAM_FIELD, transmitter.getBirthday());
            }

            settingView.showProgressDialog();

            disposables.add(
                    getCurrentUserToken()
                            .flatMap(token -> Server.getInstance().setProfileFields(token, parameters))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setProfileInfoAnswer -> {
                                if (setProfileInfoAnswer.getError_texts().size() > 0) {
                                    List<Integer> errorsCodes = setProfileInfoAnswer.getErrors();
                                    Map<Integer, String> errors_texts = setProfileInfoAnswer.getError_texts();

                                    int errorСounter = 0;

                                    if (transmitter.isBirthday_changed()) {
                                        int currentErrorCode = errorsCodes.get(errorСounter);

                                        if (currentErrorCode != 0) {
                                            settingView.showError("Ошибка смены даты.");
                                        }

                                        errorСounter++;
                                    }

                                    if (transmitter.isSurnameChanged()) {
                                        int currentErrorCode = errorsCodes.get(errorСounter);

                                        if (currentErrorCode != 0) {
                                            settingView.setChangeSurnameError(errors_texts.get(currentErrorCode));
                                        }

                                        errorСounter++;
                                    }

                                    if (transmitter.isNameChanged()) {
                                        int currentErrorCode = errorsCodes.get(errorСounter);

                                        if (currentErrorCode != 0) {
                                            settingView.setChangeNameError(errors_texts.get(currentErrorCode));
                                        }

                                        errorСounter++;
                                    }


                                    settingView.closeProgressDialog();
                                    return;
                                }

                                settingView.closeProgressDialog();
                                settingView.showError("Изменения успешно сохранены.");
                            }, error ->
                            {
                                settingView.closeProgressDialog();
                                settingView.showError(error.getMessage());
                            })
            );
        }
    }


    public void tryChangeLoginInfo(LoginInfoChangeTransmitter transmitter) {
        settingView.cleanAllErrors();

        if (transmitter.isLoginIsChanged() || transmitter.isPassIsChanged()) {
            if (TextUtils.isEmpty(transmitter.getCurrentPass())) {
                settingView.setCurrentPasswordError("Поле с паролем не может быть пусто.");
                return;
            }

            if (!TextUtils.isEmpty(transmitter.getNewPass()) && TextUtils.isEmpty(transmitter.getConfirmNewPass())) {
                settingView.setConfirmNewPasswordError("Введите повторно новый пароль.");
                return;
            }

            if (!transmitter.getNewPass().equals(transmitter.getConfirmNewPass())) {
                settingView.setConfirmNewPasswordError("Пароли не совпадают.");
                return;
            }

            Map<String, String> parameters = new LinkedHashMap<>();

            if (transmitter.isPassIsChanged()) {
                parameters.put(NEW_PASS_FILED, transmitter.getNewPass());
            }

            if (transmitter.isLoginIsChanged()) {
                parameters.put(NEW_LOGIN_FIELD, transmitter.getNewLogin());
            }

            settingView.showProgressDialog();

            disposables.add(
                    getCurrentUserToken()
                            .flatMap(token -> Server.getInstance().setLoginFields(token, transmitter.getCurrentPass(), parameters))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setProfileInfoAnswer -> {
                                if (setProfileInfoAnswer.getError() != 0) {
                                    settingView.setCurrentPasswordError(
                                            setProfileInfoAnswer.getErrorText() != null ?
                                                    setProfileInfoAnswer.getErrorText() :
                                                    Integer.toString(setProfileInfoAnswer.getError()
                                                    ));

                                    settingView.closeProgressDialog();

                                    return;
                                }

                                if (setProfileInfoAnswer.getError_texts().size() > 0) {
                                    List<Integer> errorsCodes = setProfileInfoAnswer.getErrors();
                                    Map<Integer, String> errors_texts = setProfileInfoAnswer.getError_texts();

                                    int errorСounter = 0;

                                    if (transmitter.isLoginIsChanged()) {
                                        int currentErrorCode = errorsCodes.get(errorСounter);

                                        if (currentErrorCode != 0) {
                                            settingView.setNewPasswordError(errors_texts.get(currentErrorCode));
                                        }

                                        errorСounter++;
                                    }

                                    if (transmitter.isPassIsChanged()) {
                                        int currentErrorCode = errorsCodes.get(errorСounter);

                                        if (currentErrorCode != 0) {
                                            settingView.setNewLoginError(errors_texts.get(currentErrorCode));
                                        }

                                        errorСounter++;
                                    }

                                    settingView.closeProgressDialog();

                                    return;
                                }

                                settingView.clearAllChangeLoginFields();
                                settingView.closeProgressDialog();
                                settingView.showError("Изменения успешно сохранены.");

                            }, error ->
                            {
                                settingView.closeProgressDialog();
                                settingView.showError(error.getMessage());
                            })
            );
        }
    }

    public void saveProfileIcon(File imageFile) {
        settingView.closeImageSetupDialog();
        settingView.showProgressDialog();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", "avatar", fileReqBody);

        disposables.add(getCurrentUserToken()
                .flatMap(token -> Server.getInstance().setAvatar(token, part))
                //.flatMap(urlAnswer -> saveCurrentUserAvatarUrl(urlAnswer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(urlAnswer -> {
                    if(urlAnswer.getError() != 0){
                        settingView.showError(urlAnswer.getErrorText());
                        settingView.closeProgressDialog();
                        return;
                    }

                    settingView.showError("all ok");
                    settingView.closeProgressDialog();
                }, error -> {
                    settingView.showError("Соединение с сервером говно.");
                    settingView.closeProgressDialog();
                }, () -> {
                    settingView.closeProgressDialog();
                })
        );
    }

    @SuppressLint("CheckResult")
    public void fillFieldsCurrentUserFullProfileInfo() {
        getCurrentUserFullProfileInfoFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullProfileInfo -> settingView.fillingFieldsCurrentData(fullProfileInfo.getName(), fullProfileInfo.getSurname()));
    }

    @SuppressLint("CheckResult")
    public void logout() {
        disposables.add(
                delCurrentUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(delResult -> settingView.openLoginScreen())
        );
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        disposables.dispose();
    }
}
