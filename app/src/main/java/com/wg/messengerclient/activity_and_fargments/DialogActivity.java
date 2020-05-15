package com.wg.messengerclient.activity_and_fargments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.wg.messengerclient.AddingPortToUrl;
import com.wg.messengerclient.R;
import com.wg.messengerclient.adapter.MessagesAdapter;
import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.database.entities.MessageDbEntity;
import com.wg.messengerclient.mvp_interfaces.IDialogView;
import com.wg.messengerclient.presenters.CacheKeeper;
import com.wg.messengerclient.presenters.DialogPresenter;
import com.wg.messengerclient.presenters.messageSystem.MessageSystemSingleton;

import java.util.Collection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DialogActivity extends AppCompatActivity implements IDialogView {
    private DialogPresenter presenter;

    private Toolbar toolbar;

    private EditText newMsgEditText;

    private TextView title;

    private Button sendMsgButton;

    private RecyclerView messagesRecyclerView;

    private DialogWidthMessagesLink dialog;

    private MessageSystemSingleton messageSystemSingleton;

    private MessagesAdapter messagesAdapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        messageSystemSingleton = MessageSystemSingleton.getInstance();

        Integer dialogId = (Integer) getIntent().getExtras().get(messageSystemSingleton.DIALOG_ID_EXTRA_TAG);

        dialog = messageSystemSingleton.getSpecificDialog(dialogId);

        presenter = new DialogPresenter(this, dialogId);
        getLifecycle().addObserver(presenter);

        toolbar = findViewById(R.id.dialogToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_blue);

        Glide.with(getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .load(AddingPortToUrl.addPort(dialog.dialogDbEntity.getInterlocutor().getAvatarUrl(), "1488"))
                .into((ImageView) findViewById(R.id.dialog_ava));

        FullProfileInfo profileInfo = dialog.dialogDbEntity.getInterlocutor();

        title = findViewById(R.id.dialog_title);
        title.setText("@" + profileInfo.getLogin() + " " + profileInfo.getName() + " " + profileInfo.getSurname());

        messagesRecyclerView = findViewById(R.id.messages_recyclerView);
        newMsgEditText = findViewById(R.id.message_textEdit);
        sendMsgButton = findViewById(R.id.send_msg);

        sendMsgButton.setOnClickListener(v -> presenter.trySendMessage(newMsgEditText.getText().toString()));

        CacheKeeper cacheKeeper = new CacheKeeper();
        cacheKeeper
                .getCurrentUserFullProfileInfoFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    messagesAdapter = new MessagesAdapter(user.getId());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);
                    messagesRecyclerView.setLayoutManager(linearLayoutManager);
                    messagesRecyclerView.setAdapter(messagesAdapter);
                    messagesAdapter.setAllMsg(dialog.messages);
                    messagesRecyclerView.smoothScrollToPosition(messagesRecyclerView.getAdapter().getItemCount());
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //todo
    @Override
    public void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNewMessages(Collection<MessageDbEntity> newMessages) {
        messagesAdapter.addAllMsg(newMessages);
        messagesRecyclerView.smoothScrollToPosition(messagesRecyclerView.getAdapter().getItemCount());
    }

    @Override
    public void clearNewMsgTextEdit() {
        newMsgEditText.setText("");
        messagesRecyclerView.smoothScrollToPosition(messagesRecyclerView.getAdapter().getItemCount());
    }
}
