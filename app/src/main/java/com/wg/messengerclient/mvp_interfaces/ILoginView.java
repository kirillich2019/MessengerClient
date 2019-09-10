package com.wg.messengerclient.mvp_interfaces;

public interface ILoginView extends ILoadingView {
    void openNextScreen();

    void showLoginError(String errorText);
}
