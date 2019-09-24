package com.wg.messengerclient.mvp_interfaces;

public interface IProfileInfoView extends IShowError, IShowWarning {
    void setProfileInfo(String name, String surname, String login, String birthday, String status);

    void onFragmentShow();
}
