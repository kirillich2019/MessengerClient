package com.wg.messengerclient.activity_and_fargments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.wg.messengerclient.R;
import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.presenters.messageSystem.MessageSystemSingleton;

public class DialogActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private MessageSystemSingleton messageSystemSingleton;

    private DialogWidthMessagesLink dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        messageSystemSingleton = MessageSystemSingleton.getInstance();

        Integer dialogId = (Integer) getIntent().getExtras().get(messageSystemSingleton.DIALOG_ID_EXTRA_TAG);

        dialog = messageSystemSingleton.getSpecificDialog(dialogId);

        toolbar = findViewById(R.id.dialogToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FullProfileInfo profileInfo = dialog.dialogDbEntity.getInterlocutor();
        getSupportActionBar().setTitle("@" + profileInfo.getLogin() + " " + profileInfo.getName() + " " + profileInfo.getSurname());
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
}
