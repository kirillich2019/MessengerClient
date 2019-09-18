package com.wg.messengerclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.presenters.ProfileInfoPresenter;

public class UserProfileFragment extends Fragment implements IProfileInfoView {
    private ProfileInfoPresenter profileInfoPresenter;
    private TextView fullName, birthday_text, status_text, login_text;
    private ExpandableRelativeLayout expandableRelativeLayout;
    private Button openInfoButton;
    private Drawable left, rightOpen, rightClose;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fullName = getActivity().findViewById(R.id.full_name_textView);
        birthday_text = getActivity().findViewById(R.id.birthday_textView);
        status_text = getActivity().findViewById(R.id.status_textView);
        login_text = getActivity().findViewById(R.id.login_textView);

        expandableRelativeLayout = getActivity().findViewById(R.id.expandableLayout);
        expandableRelativeLayout.collapse();
        openInfoButton = getActivity().findViewById(R.id.information_spoiler);
        openInfoButton.setOnClickListener(v -> expandableRelativeLayout.toggle());

        left = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.info_drawable_left);
        left.setBounds(
                0,
                0,
                left.getIntrinsicWidth(),
                left.getIntrinsicHeight()
        );

        rightOpen = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.expandable_drawable_right_open);
        rightOpen.setBounds(
                0, 0,
                rightOpen.getIntrinsicWidth(),
                rightOpen.getIntrinsicHeight()

        );

        rightClose = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.expandable_drawable_right_close);
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

        if (birthday != null) {
            birthday_text.setText("birthday: " + birthday);
            birthday_text.setVisibility(View.VISIBLE);
        } else
            birthday_text.setVisibility(View.GONE);

        if (login != null) {
            login_text.setText("login: " + login);
            login_text.setVisibility(View.VISIBLE);
        } else
            login_text.setVisibility(View.GONE);
    }
}
