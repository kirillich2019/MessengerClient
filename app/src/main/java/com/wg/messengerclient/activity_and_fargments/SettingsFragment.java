package com.wg.messengerclient.activity_and_fargments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wg.messengerclient.R;
import com.wg.messengerclient.mvp_interfaces.ISettingView;
import com.wg.messengerclient.presenters.ProfileChangesTransmitter;
import com.wg.messengerclient.presenters.SettingsPresenter;

public class SettingsFragment extends Fragment implements ISettingView {
    private SettingsPresenter presenter;
    private EditText nameEditText, surnameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameEditText = getActivity().findViewById(R.id.change_name_textEdit);
        surnameEditText = getActivity().findViewById(R.id.change_surname_textEdit);

        getActivity().findViewById(R.id.exit_from_application).setOnClickListener(v -> presenter.logout());

        getActivity().findViewById(R.id.save_profile_settings).setOnClickListener(v -> {
            ProfileChangesTransmitter transmitter = new ProfileChangesTransmitter(
                    "name",
                    "surname",
                    "dr",
                    true,
                    true,
                    true);

            presenter.tryChangeProfileInfo(transmitter);
        });

        presenter = new SettingsPresenter(this);
        getLifecycle().addObserver(presenter);
    }

    @Override
    public void openLoginScreen() {
        Intent loginActivity = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().getApplicationContext().startActivity(loginActivity);
    }

    @Override
    public void fillingFieldsCurrentData(String name, String surname) {
        nameEditText.setText(name);
        surnameEditText.setText(surname);
    }

    @Override
    public void onFragmentShow() {
        presenter.fillFieldsCurrentUserFullProfileInfo();
    }


    @Override
    public void showError(String errorText) {
        Toast.makeText(getActivity().getApplicationContext(), errorText, Toast.LENGTH_LONG).show();
    }
}
