package com.wg.messengerclient.activity_and_fargments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wg.messengerclient.R;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.interfaces.IOpenUserProfile;
import com.wg.messengerclient.long_pol_actions.DistributeRxEventBus;
import com.wg.messengerclient.models.server_answers.ActionType;
import com.wg.messengerclient.mvp_interfaces.IChatsView;
import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.mvp_interfaces.ISettingView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainApplicationScreenActivity extends AppCompatActivity implements IOpenUserProfile {
    private FragmentManager fragmentManager;
    private Fragment profileInfo, settings, friends, chatsFragment;
    private IProfileInfoView profileInfoView;
    private ISettingView settingView;
    private IChatsView chatsView;
    final static String MY_PROFILE_INFO_FRAGMENT_TAG = "PROFILE_INFO";
    final static String SETTINGS_FRAGMENT_TAG = "SETTINGS";
    final static String FRIENDS_FRAGMENT_TAG = "FRIENDS";
    final static String USER_PROFILE_FRAGMENT_TAG = "USER_PROFILE";
    final static String CHATS_FRAGMENT_TAG = "CHATS";

    private String currentFragmentTag;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application_screen);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fragmentManager = getSupportFragmentManager();

        profileInfo = new UserProfileFragment();
        profileInfoView = (IProfileInfoView) profileInfo;

        settings = new SettingsFragment();
        settingView = (ISettingView) settings;

        friends = new FriendsFragment(this);

        chatsFragment = new ChatFragment();
        chatsView = (IChatsView) chatsFragment;

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment, profileInfo, MY_PROFILE_INFO_FRAGMENT_TAG)
                .add(R.id.main_fragment, settings, SETTINGS_FRAGMENT_TAG)
                .add(R.id.main_fragment, friends, FRIENDS_FRAGMENT_TAG)
                .add(R.id.main_fragment, chatsFragment, CHATS_FRAGMENT_TAG)
                .hide(settings)
                .hide(friends)
                .hide(chatsFragment)
                .commit();

        currentFragmentTag = MY_PROFILE_INFO_FRAGMENT_TAG;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean openFriendsFragment = (Boolean) extras.get("openFriendsFragment");

            if (openFriendsFragment != null && openFriendsFragment){
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
                bottomNavigationView.setSelectedItemId(R.id.friends_fragment);
                openFriendsFragment();
            }
        }

        ((BottomNavigationView) findViewById(R.id.bottom_nav_view)).setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_fragment:
                    if (currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
                        closeUserProfileFragmentIfOpen();

                    fragmentManager.beginTransaction()
                            .show(profileInfo)
                            .hide(settings)
                            .hide(friends)
                            .hide(chatsFragment)
                            .commit();

                    currentFragmentTag = MY_PROFILE_INFO_FRAGMENT_TAG;
                    profileInfoView.onFragmentShow();
                    break;
                case R.id.friends_fragment:
                    openFriendsFragment();
                    break;
                case R.id.messages_fragment:
                    openChatsFragment();
                    break;
                case R.id.settings_fragment:
                    if (currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
                        closeUserProfileFragmentIfOpen();

                    fragmentManager.beginTransaction()
                            .show(settings)
                            .hide(profileInfo)
                            .hide(friends)
                            .hide(chatsFragment)
                            .commit();

                    currentFragmentTag = SETTINGS_FRAGMENT_TAG;
                    settingView.onFragmentShow();
                    break;
            }

            return true;
        });

        DistributeRxEventBus.GetInstance()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    switch (action.getType()) {
                        case ActionType.FRIEND_REQUEST_RECEIVED:
                            Toast.makeText(getApplicationContext(), "Новый запрос в други.", Toast.LENGTH_SHORT).show();
                            break;
                        case ActionType.FRIEND_REQUEST_ACCEPTED:
                            Toast.makeText(getApplicationContext(), "Запрос в друзья принят", Toast.LENGTH_SHORT).show();
                            break;
                        case ActionType.MESSAGE:
                            Toast.makeText(getApplicationContext(), "Новое сообщение.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    private void openFriendsFragment() {
        if (currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
            closeUserProfileFragmentIfOpen();

        fragmentManager.beginTransaction()
                .show(friends)
                .hide(settings)
                .hide(profileInfo)
                .hide(chatsFragment)
                .commit();

        currentFragmentTag = FRIENDS_FRAGMENT_TAG;
    }

    private void openChatsFragment(){
        if (currentFragmentTag == USER_PROFILE_FRAGMENT_TAG)
            closeUserProfileFragmentIfOpen();

        fragmentManager.beginTransaction()
                .show(chatsFragment)
                .hide(settings)
                .hide(profileInfo)
                .hide(friends)
                .commit();

        chatsView.onFragmentShow();

        currentFragmentTag = CHATS_FRAGMENT_TAG;
    }

    private void closeUserProfileFragmentIfOpen() {
        Fragment oldUserProfileFragment = fragmentManager.findFragmentByTag(USER_PROFILE_FRAGMENT_TAG);

        if (oldUserProfileFragment != null)
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
