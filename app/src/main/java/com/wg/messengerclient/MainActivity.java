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
    private Button loginButton;
    private Button regButton;
    private EditText loginTextField;
    private EditText passwordTextField;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        regButton = findViewById(R.id.regButton);
        loginTextField = findViewById(R.id.loginField);
        passwordTextField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(v -> {
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Observable.fromCallable(() -> {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        return "hellow";
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                        loginButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    });

            /*MyServer.getInstance().welcome()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(welcomeAnswer -> {
                        Toast.makeText(this, welcomeAnswer.text, Toast.LENGTH_LONG).show();
                    }, error -> {
                        error.printStackTrace();
                    });*/
        });

        regButton.setOnClickListener(v -> {
            Intent regActivity = new Intent(this, Registration.class);
            startActivity(regActivity);
        });

    }

}
