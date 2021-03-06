package com.wg.messengerclient.mvp_interfaces;

import android.content.Context;

public interface ISettingView extends IShowError{
    void openLoginScreen();

    void fillingFieldsCurrentData(String name, String surname, String profileImageUrl);

    void onFragmentShow();

    void cleanAllErrors();

    void setChangeNameError(String text);

    void setChangeSurnameError(String text);

    void setCurrentPasswordError(String text);

    void setNewPasswordError(String text);

    void setConfirmNewPasswordError(String text);

    void setNewLoginError(String text);

    void clearAllChangeLoginFields();

    void clearAllChangeProfileInfoFields();

    void showProgressDialog();

    void closeProgressDialog();

    void showImageSetupDialog();

    void closeImageSetupDialog();

    void setupProfileAvatar(String url);

    Context getContext();
}
