package com.wg.messengerclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class MainActivity extends AppCompatActivity {
    private MyServer server;

    private Button loginButton;
    private Button regButton;
    private EditText loginTextField;
    private EditText passwordTextField;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = MyServer.getInstance();

        loginButton = findViewById(R.id.loginButton);
        regButton = findViewById(R.id.regButton);
        loginTextField = findViewById(R.id.loginField);
        passwordTextField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(v -> {
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            server.login("\"" + loginTextField.getText().toString() + "\"", "\"" + passwordTextField.getText().toString() + "\"")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loginAnswer -> {
                        if (loginAnswer.error == 0){
                            Toast.makeText(this, "token:" + loginAnswer.token, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(this, "errorCode:" + loginAnswer.error, Toast.LENGTH_LONG).show();
                        }

                        loginButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }, error -> {
                        error.printStackTrace();
                        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
                        loginButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    });
        });

        regButton.setOnClickListener(v -> {
            Intent regActivity = new Intent(this, Registration.class);
            startActivity(regActivity);
        });
    }
}
