package com.wg.messengerclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainApplicationScreenActivity extends AppCompatActivity{
    private FragmentManager fragmentManager;
    private Fragment profileInfo, settings;
    final static String PROFILE_INFO_FRAGMENT_TAG = "PROFILE_INFO";
    final static String SETTINGS_FRAGMENT_TAG = "SETTINGS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application_screen);

        fragmentManager = getSupportFragmentManager();

        profileInfo = new UserProfileFragment();
        settings = new SettingsFragment();

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
                    break;
            }

            return true;
        });
    }
}
