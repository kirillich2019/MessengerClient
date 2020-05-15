package com.wg.messengerclient.activity_and_fargments;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wg.messengerclient.long_pol_actions.GetActionsWorker;
import com.wg.messengerclient.R;
import com.wg.messengerclient.mvp_interfaces.ILoginView;
import com.wg.messengerclient.presenters.LoginPresenter;
import com.wg.messengerclient.presenters.messageSystem.MessageSystemSingleton;

@SuppressLint("CheckResult")
public class LoginActivity extends AppCompatActivity implements ILoginView {
    private LoginPresenter loginPresenter;

    private Button loginButton;
    private Button regButton;
    private EditText loginTextField;
    private EditText passwordTextField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginPresenter = new LoginPresenter(this, getApplicationContext());
        getLifecycle().addObserver(loginPresenter);

        loginButton = findViewById(R.id.loginButton);
        regButton = findViewById(R.id.regButton);
        loginTextField = findViewById(R.id.loginField);
        passwordTextField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.progress_bar);
        loginButton.setOnClickListener(v -> loginPresenter.tryLogin(loginTextField.getText().toString(), passwordTextField.getText().toString()));

        regButton.setOnClickListener(v -> {
            Intent regActivity = new Intent(this, RegistrationActivity.class);
            startActivity(regActivity);
        });
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        loginButton.setVisibility(View.INVISIBLE);
        loginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeLoading() {
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void openNextScreen() {
        GetActionsWorker.startWorker(getApplicationContext());

        Intent mainAppActivity = new Intent(getApplicationContext(), MainApplicationScreenActivity.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean openFriendsFragment = (Boolean) extras.get("openFriendsFragment");

            if (openFriendsFragment != null && openFriendsFragment)
                mainAppActivity.putExtra("openFriendsFragment", true);
        }

        mainAppActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(mainAppActivity);
        finish();
    }
}
