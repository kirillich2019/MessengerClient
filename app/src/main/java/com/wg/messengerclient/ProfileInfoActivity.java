package com.wg.messengerclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.wg.messengerclient.mvp_interfaces.IProfileInfoView;
import com.wg.messengerclient.presenters.ProfileInfoPresenter;

public class ProfileInfoActivity extends AppCompatActivity implements IProfileInfoView {
    private ProfileInfoPresenter profileInfoPresenter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        textView = findViewById(R.id.testTextView);

        profileInfoPresenter = new ProfileInfoPresenter(this);
    }

    @Override
    public void setText(String text) {
        textView.setText(text);
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }
}
