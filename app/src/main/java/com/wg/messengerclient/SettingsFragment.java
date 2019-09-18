package com.wg.messengerclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wg.messengerclient.mvp_interfaces.ISettingView;
import com.wg.messengerclient.presenters.SettingsPresenter;

public class SettingsFragment extends Fragment implements ISettingView {
    private SettingsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.exit_from_application).setOnClickListener(v -> presenter.logout());

        presenter = new SettingsPresenter(this);
        getLifecycle().addObserver(presenter);
    }

    @Override
    public void openLoginScreen() {
        Intent loginActivity = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().getApplicationContext().startActivity(loginActivity);
    }
}
