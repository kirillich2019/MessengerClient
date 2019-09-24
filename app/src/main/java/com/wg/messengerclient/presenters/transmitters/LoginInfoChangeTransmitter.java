package com.wg.messengerclient.presenters.transmitters;

public class LoginInfoChangeTransmitter {
    private String
            currentPass,
            newLogin,
            newPass,
            confirmNewPass;

    private boolean
            loginIsChanged,
            passIsChanged;

    public String getCurrentPass() {
        return currentPass;
    }

    public String getNewLogin() {
        return newLogin;
    }

    public String getNewPass() {
        return newPass;
    }

    public String getConfirmNewPass() {
        return confirmNewPass;
    }

    public boolean isLoginIsChanged() {
        return loginIsChanged;
    }

    public boolean isPassIsChanged() {
        return passIsChanged;
    }

    public LoginInfoChangeTransmitter(
            String currentPass,
            String newLogin,
            String newPass,
            String confirmNewPass,
            boolean loginIsChanged,
            boolean passIsChanged)
    {
        this.currentPass = currentPass;
        this.newLogin = newLogin;
        this.newPass = newPass;
        this.confirmNewPass = confirmNewPass;
        this.loginIsChanged = loginIsChanged;
        this.passIsChanged = passIsChanged;
    }
}
