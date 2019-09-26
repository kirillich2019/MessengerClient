package com.wg.messengerclient.mvp_interfaces;

import android.content.Context;

public interface IProfileInfoView extends IShowError, IShowWarning {
    void setProfileInfo(String name, String surname, String login, String birthday, String status);

    void setUserAvatar(String avatarUrl);

    void onFragmentShow();

    Context getAppContext();
}
