package com.wg.messengerclient.activity_and_fargments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wg.messengerclient.R;
import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.mvp_interfaces.ISettingView;

public class MainApplicationScreenActivity extends AppCompatActivity{
    private FragmentManager fragmentManager;
    private Fragment profileInfo, settings;
    private IProfileInfoView profileInfoView;
    private ISettingView settingView;
    final static String PROFILE_INFO_FRAGMENT_TAG = "PROFILE_INFO";
    final static String SETTINGS_FRAGMENT_TAG = "SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application_screen);

        fragmentManager = getSupportFragmentManager();

        profileInfo = new UserProfileFragment();
        profileInfoView = (IProfileInfoView)profileInfo;
        settings = new SettingsFragment();
        settingView = (ISettingView)settings;

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment, profileInfo, PROFILE_INFO_FRAGMENT_TAG)
                .add(R.id.main_fragment, settings, SETTINGS_FRAGMENT_TAG)
                .hide(settings)
                .commit();

        ((BottomNavigationView) findViewById(R.id.bottom_nav_view)).setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_fragment:
                    fragmentManager.beginTransaction()
                            .show(profileInfo)
                            .hide(settings)
                            .commit();

                    profileInfoView.onFragmentShow();
                    break;
                case R.id.friends_fragment:

                    break;
                case R.id.messages_fragment:

                    break;
                case R.id.settings_fragment:
                    fragmentManager.beginTransaction()
                            .show(settings)
                            .hide(profileInfo)
                            .commit();

                    settingView.onFragmentShow();
                    break;
            }

            return true;
        });
    }
}
