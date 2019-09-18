package com.wg.messengerclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wg.messengerclient.mvp_interfaces.IRegistrationView;
import com.wg.messengerclient.presenters.RegistrationPresenter;

public class RegistrationActivity extends AppCompatActivity implements IRegistrationView {
    private RegistrationPresenter registrationPresenter;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private Button regButton;
    private EditText reg_login,
            reg_pass,
            confirm_reg_pass,
            reg_name,
            reg_surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.scroll_from_top, R.anim.scroll_from_top);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationPresenter = new RegistrationPresenter(this);
        getLifecycle().addObserver(registrationPresenter);

        progressBar = findViewById(R.id.reg_progress_bar);
        reg_login = findViewById(R.id.reg_login);
        reg_pass = findViewById(R.id.reg_pass);
        confirm_reg_pass = findViewById(R.id.confirm_reg_pass);
        reg_name = findViewById(R.id.reg_name);
        reg_surname = findViewById(R.id.reg_surname);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.scroll_from_top, R.anim.scroll_to_top);});

        regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(v ->
                registrationPresenter.tryRegistration(reg_login.getText().toString(),
                reg_pass.getText().toString(), confirm_reg_pass.getText().toString(),
                reg_name.getText().toString(), reg_surname.getText().toString()));
    }

    @Override
    public void showLoading() {
        regButton.setEnabled(false);
        regButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeLoading() {
        regButton.setEnabled(true);
        regButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void openNextScreen() {
        Intent regActivity = new Intent(getApplicationContext(), ProfileInfoActivity.class);
        getApplicationContext().startActivity(regActivity);
    }
}
