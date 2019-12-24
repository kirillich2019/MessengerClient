package com.wg.messengerclient.activity_and_fargments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wg.messengerclient.R;
import com.wg.messengerclient.models.server_answers.MessagesAnswer;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;
import com.wg.messengerclient.mvp_interfaces.IMessagesView;
import com.wg.messengerclient.presenters.CacheKeeper;
import com.wg.messengerclient.server.Server;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessagesFragment extends Fragment implements IMessagesView {
    private TextView testMsgTextView;
    private CacheKeeper cacheKeeper;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cacheKeeper = new CacheKeeper();

        testMsgTextView = view.findViewById(R.id.test_msg_textView);

        button = view.findViewById(R.id.getMessagesButton);
        button.setOnClickListener(v -> {
            cacheKeeper.getCurrentUserToken()
                    .flatMap(token -> Server.getInstanceShortOperationsServer().getMessages(token, 3, 1, 100))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messagesList -> {
                        ArrayList<MessagesAnswer> list = (ArrayList<MessagesAnswer>)messagesList;
                                showMessage(list.get(list.size() - 1).getText());
                            }, error -> {
                                showError(error.getMessage());}
                    );
        });
    }

    @Override
    public void showMessage(String text) {
        testMsgTextView.setText(text);
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_LONG).show();
    }
}
