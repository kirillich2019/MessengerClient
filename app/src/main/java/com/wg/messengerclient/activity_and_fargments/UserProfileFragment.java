package com.wg.messengerclient.activity_and_fargments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.navigation.NavigationView;
import com.wg.messengerclient.R;
import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.presenters.ProfileInfoPresenter;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment implements IProfileInfoView {
    private ProfileInfoPresenter profileInfoPresenter;
    private TextView fullName, birthday_text, status_text, login_text, warningText;
    private ExpandableRelativeLayout expandableRelativeLayout;
    private Button openInfoButton;
    private Drawable left, rightOpen, rightClose;
    private CardView warningPanel;
    private ImageView profileAvatar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileAvatar = view.findViewById(R.id.profile_image);

        fullName = view.findViewById(R.id.full_name_textView);
        birthday_text = view.findViewById(R.id.birthday_textView);
        status_text = view.findViewById(R.id.status_textView);
        login_text = view.findViewById(R.id.login_textView);
        warningText = view.findViewById(R.id.warning_text);
        warningPanel = view.findViewById(R.id.warningCardView);

        expandableRelativeLayout = view.findViewById(R.id.expandableLayout);
        expandableRelativeLayout.collapse();
        openInfoButton = view.findViewById(R.id.information_spoiler);
        openInfoButton.setOnClickListener(v -> expandableRelativeLayout.toggle());

        left = ContextCompat.getDrawable(getContext(), R.drawable.info_drawable_left);
        left.setBounds(
                0,
                0,
                left.getIntrinsicWidth(),
                left.getIntrinsicHeight()
        );

        rightOpen = ContextCompat.getDrawable(getContext(), R.drawable.expandable_drawable_right_open);
        rightOpen.setBounds(
                0, 0,
                rightOpen.getIntrinsicWidth(),
                rightOpen.getIntrinsicHeight()

        );

        rightClose = ContextCompat.getDrawable(getContext(), R.drawable.expandable_drawable_right_close);
        rightClose.setBounds(
                0,
                0,
                rightClose.getIntrinsicWidth(),
                rightClose.getIntrinsicHeight()
        );

        setOpenInfoButtonDrawables(false);
        expandableRelativeLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {

            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {
                setOpenInfoButtonDrawables(false);
            }

            @Override
            public void onClosed() {
                setOpenInfoButtonDrawables(true);
            }
        });


        profileAvatar.setOnClickListener( v -> profileInfoPresenter.showFullSizeProfilePhoto());

        profileInfoPresenter = new ProfileInfoPresenter(this);
    }

    private void setOpenInfoButtonDrawables(boolean isOpen) {
        openInfoButton.setCompoundDrawables(
                left,
                null,
                isOpen ? rightOpen : rightClose,
                null
        );
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getActivity().getApplicationContext(), errorText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProfileInfo(String name, String surname, String login, String birthday, String status) {
        String fullNameString = "";

        if (name != null)
            fullNameString += name;

        if (surname != null)
            fullNameString += " " + surname;

        fullName.setText(fullNameString);

        if (status != null) {
            status_text.setText(status);
        } else
            status_text.setVisibility(View.INVISIBLE);

        birthday_text.setText("birthday: " + (birthday != null ? birthday : "не указано"));

        if (login != null) {
            login_text.setText("login: " + login);
            login_text.setVisibility(View.VISIBLE);
        } else
            login_text.setVisibility(View.GONE);
    }

    @Override
    public void setUserAvatar(String avatarUrl) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.circleCrop();

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(avatarUrl)
                .into(profileAvatar);
    }

    @Override
    public void onFragmentShow() {
        profileInfoPresenter.initialFilling();
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    @Override
    public void hideWarning() {
        warningPanel.setVisibility(View.GONE);
    }

    @Override
    public void showWarning(String warningText) {
        warningPanel.setVisibility(View.VISIBLE);
        this.warningText.setText(warningText);
    }
}
