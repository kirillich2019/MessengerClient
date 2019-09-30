package com.wg.messengerclient.activity_and_fargments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wg.messengerclient.R;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.interfaces.IOpenUserProfile;
import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.mvp_interfaces.ISettingView;

public class MainApplicationScreenActivity extends AppCompatActivity implements IOpenUserProfile {
    private FragmentManager fragmentManager;
    private Fragment profileInfo, settings, friends;
    private IProfileInfoView profileInfoView;
    private ISettingView settingView;
    final static String MY_PROFILE_INFO_FRAGMENT_TAG = "PROFILE_INFO";
    final static String SETTINGS_FRAGMENT_TAG = "SETTINGS";
    final static String FRIENDS_FRAGMENT_TAG = "FRIENDS";
    final static String USER_PROFILE_FRAGMENT_TAG = "USER_PROFILE";
    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fragmentManager = getSupportFragmentManager();

        profileInfo = new UserProfileFragment();
        profileInfoView = (IProfileInfoView)profileInfo;
        settings = new SettingsFragment();
        settingView = (ISettingView)settings;
        friends = new FriendsFragment(this);

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment, profileInfo, MY_PROFILE_INFO_FRAGMENT_TAG)
                .add(R.id.main_fragment, settings, SETTINGS_FRAGMENT_TAG)
                .add(R.id.main_fragment, friends, FRIENDS_FRAGMENT_TAG)
                .hide(settings)
                .hide(friends)
                .commit();

        currentFragmentTag = MY_PROFILE_INFO_FRAGMENT_TAG;

        ((BottomNavigationView) findViewById(R.id.bottom_nav_view)).setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_fragment:
                    if(currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
                        closeUserProfileFragmentIfOpen();

                    fragmentManager.beginTransaction()
                            .show(profileInfo)
                            .hide(settings)
                            .hide(friends)
                            .commit();

                    currentFragmentTag = MY_PROFILE_INFO_FRAGMENT_TAG;
                    profileInfoView.onFragmentShow();
                    break;
                case R.id.friends_fragment:
                    if(currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
                        closeUserProfileFragmentIfOpen();

                    fragmentManager.beginTransaction()
                            .show(friends)
                            .hide(settings)
                            .hide(profileInfo)
                            .commit();

                    currentFragmentTag = FRIENDS_FRAGMENT_TAG;
                    break;
                case R.id.messages_fragment:

                    break;
                case R.id.settings_fragment:
                    if(currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
                        closeUserProfileFragmentIfOpen();

                    fragmentManager.beginTransaction()
                            .show(settings)
                            .hide(profileInfo)
                            .hide(friends)
                            .commit();

                    currentFragmentTag = SETTINGS_FRAGMENT_TAG;
                    settingView.onFragmentShow();
                    break;
            }

            return true;
        });
    }

    private void closeUserProfileFragmentIfOpen(){
        Fragment oldUserProfileFragment = fragmentManager.findFragmentByTag(USER_PROFILE_FRAGMENT_TAG);

        if(oldUserProfileFragment != null)
            fragmentManager.beginTransaction()
            .remove(oldUserProfileFragment)
            .commit();
    }

    @Override
    public void open(FullProfileInfo fullProfileInfo) {
        currentFragmentTag = USER_PROFILE_FRAGMENT_TAG;

        UserProfileFragment newUserProfileFragment = new UserProfileFragment(fullProfileInfo);

        fragmentManager.beginTransaction()
                .hide(profileInfo)
                .hide(settings)
                .hide(friends)
                .add(R.id.main_fragment, newUserProfileFragment, USER_PROFILE_FRAGMENT_TAG)
                .show(newUserProfileFragment)
                .commit();
    }
}
