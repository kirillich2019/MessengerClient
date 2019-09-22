package com.wg.messengerclient.mvp_interfaces;

public interface ISettingView extends IShowError{
    void openLoginScreen();

    void fillingFieldsCurrentData(String name, String surname);

    void onFragmentShow();
}
