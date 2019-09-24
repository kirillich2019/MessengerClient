package com.wg.messengerclient.activity_and_fargments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.wg.messengerclient.R;
import com.wg.messengerclient.mvp_interfaces.ISettingView;
import com.wg.messengerclient.presenters.transmitters.LoginInfoChangeTransmitter;
import com.wg.messengerclient.presenters.transmitters.ProfileInfoChangesTransmitter;
import com.wg.messengerclient.presenters.SettingsPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsFragment extends Fragment implements ISettingView {
    private SettingsPresenter presenter;
    private EditText
            nameEditText,
            surnameEditText,
            currentPasswordEditText,
            newPasswordEditText,
            confirmNewPasswordEditText,
            newLoginEditText;
    private TextInputLayout
            changeNameInputLayout,
            changeSurnameInputLayout,
            currentPasswordInputLayout,
            newPasswordInputLayout,
            confirmNewPasswordInputLayout,
            newLoginInputLayout;
    private String birthday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //editText
        nameEditText = view.findViewById(R.id.change_name_textEdit);
        surnameEditText = view.findViewById(R.id.change_surname_textEdit);
        currentPasswordEditText = view.findViewById(R.id.сurrent_password);
        newPasswordEditText = view.findViewById(R.id.new_password);
        confirmNewPasswordEditText = view.findViewById(R.id.confirm_new_password);
        newLoginEditText = view.findViewById(R.id.new_login);

        //textInputLayout
        changeNameInputLayout = view.findViewById(R.id.change_name_til);
        changeSurnameInputLayout = view.findViewById(R.id.change_surname_til);
        currentPasswordInputLayout = view.findViewById(R.id.сurrent_password_till);
        newPasswordInputLayout = view.findViewById(R.id.new_password_till);
        confirmNewPasswordInputLayout = view.findViewById(R.id.confirm_new_password_till);
        newLoginInputLayout = view.findViewById(R.id.new_login_till);

        view.findViewById(R.id.exit_from_application).setOnClickListener(v -> presenter.logout());

        view.findViewById(R.id.save_profile_settings).setOnClickListener(v -> {
            ProfileInfoChangesTransmitter transmitter = new ProfileInfoChangesTransmitter(
                    nameEditText.getText().toString().trim(),
                    surnameEditText.getText().toString().trim(),
                    birthday,
                    true,
                    true,
                    birthday != null);

            presenter.tryChangeProfileInfo(transmitter);
        });

        view.findViewById(R.id.save_login_setting).setOnClickListener(v -> {
            String newLogin = newLoginEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            LoginInfoChangeTransmitter transmitter = new LoginInfoChangeTransmitter(
                    currentPasswordEditText.getText().toString().trim(),
                    newLogin,
                    newPassword,
                    confirmNewPasswordEditText.getText().toString().trim(),
                    !newLogin.isEmpty(),
                    !newPassword.isEmpty());

            presenter.tryChangeLoginInfo(transmitter);
        });

        view.findViewById(R.id.change_birthday).setOnClickListener(v -> {
            Date today = new Date();
            new DatePickerDialog(
                    getContext(),
                    (v2, year, month, dayOfMonth) -> {
                        birthday = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(
                                new Date(
                                        year - 1900,
                                        month,
                                        dayOfMonth
                                )
                        );

                    },
                    today.getYear() + 1900,
                    today.getMonth(),
                    today.getDay()
            ).show();
        });

        presenter = new SettingsPresenter(this);
        getLifecycle().addObserver(presenter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void openLoginScreen() {
        Intent loginActivity = new Intent(getContext(), LoginActivity.class);
        loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(loginActivity);
    }

    @Override
    public void fillingFieldsCurrentData(String name, String surname) {
        nameEditText.setText(name);
        surnameEditText.setText(surname);
    }

    @Override
    public void onFragmentShow() {
        presenter.fillFieldsCurrentUserFullProfileInfo();
        cleanAllErrors();
    }

    @Override
    public void cleanAllErrors() {
        changeNameInputLayout.setError("");
        changeSurnameInputLayout.setError("");
        currentPasswordInputLayout.setError("");
        newPasswordInputLayout.setError("");
        confirmNewPasswordInputLayout.setError("");
        newLoginInputLayout.setError("");
    }

    @Override
    public void setChangeNameError(String text) {
        changeNameInputLayout.setError(text);
    }

    @Override
    public void setChangeSurnameError(String text) {
        changeSurnameInputLayout.setError(text);
    }

    @Override
    public void setCurrentPasswordError(String text) {
        currentPasswordInputLayout.setError(text);
    }

    @Override
    public void setNewPasswordError(String text) {
        newLoginInputLayout.setError(text);
    }

    @Override
    public void setConfirmNewPasswordError(String text) {
        confirmNewPasswordInputLayout.setError(text);
    }

    @Override
    public void setNewLoginError(String text) {
        newLoginInputLayout.setError(text);
    }

    @Override
    public void clearAllChangeLoginFields() {
        currentPasswordEditText.setText("");
        newLoginEditText.setText("");
        newPasswordEditText.setText("");
        confirmNewPasswordEditText.setText("");
    }

    @Override
    public void clearAllChangeProfileInfoFields() {
        nameEditText.setText("");
        surnameEditText.setText("");
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_LONG).show();
    }
}
