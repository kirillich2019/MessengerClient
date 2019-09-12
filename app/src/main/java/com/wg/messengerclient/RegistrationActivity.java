package com.wg.messengerclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wg.messengerclient.mvp_interfaces.IRegistrationView;
import com.wg.messengerclient.presenters.RegistrationPresenter;

public class RegistrationActivity extends AppCompatActivity implements IRegistrationView {
    private RegistrationPresenter registrationPresenter;
    private ImageButton backButton;
    private Button regButton;
    private EditText reg_login,
            reg_pass,
            reg_name,
            reg_surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationPresenter = new RegistrationPresenter(this);
        getLifecycle().addObserver(registrationPresenter);

        reg_login = findViewById(R.id.reg_login);
        reg_pass = findViewById(R.id.reg_pass);
        reg_name = findViewById(R.id.reg_name);
        reg_surname = findViewById(R.id.reg_surname);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(v -> registrationPresenter.tryRegistration(reg_login.getText().toString(), reg_pass.getText().toString(), reg_name.getText().toString(), reg_surname.getText().toString()));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }
}
