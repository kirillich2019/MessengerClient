package com.wg.messengerclient.mvp_interfaces;

public interface IProfileInfoView extends IShowError {
    void setProfileInfo(String name, String surname, String login, String birthday, String status);
}
