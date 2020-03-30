package com.wg.messengerclient.activity_and_fargments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wg.messengerclient.R;
import com.wg.messengerclient.adapter.ChatsAdapter;
import com.wg.messengerclient.database.entities.DialogWidthMessagesLink;
import com.wg.messengerclient.mvp_interfaces.IChatsView;
import com.wg.messengerclient.presenters.ChatsPresenter;
import com.wg.messengerclient.presenters.messageSystem.MessageSystemSingleton;

import java.util.Collection;

public class ChatFragment extends Fragment implements IChatsView {
    private RecyclerView chatsRecyclerView;
    private ChatsAdapter chatsAdapter;
    private ChatsPresenter presenter;

    private TextView onLoadDialogsText;
    private ProgressBar onLoadDialogsProgressBar;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onLoadDialogsProgressBar = view.findViewById(R.id.load_chats_progressbar);
        onLoadDialogsText = view.findViewById(R.id.load_chats_text);

        chatsRecyclerView = view.findViewById(R.id.my_chats_res_view);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter = new ChatsPresenter(this);
        getLifecycle().addObserver(presenter);

        ChatsAdapter.OnChatClickListener chatClickListener = position -> {
            Intent dialogActivity = new Intent(getContext(), DialogActivity.class);
            dialogActivity.putExtra(MessageSystemSingleton.DIALOG_ID_EXTRA_TAG, chatsAdapter.getChatByPostion(position).dialogDbEntity.getDialogId());
            getContext().startActivity(dialogActivity);
        };

        chatsAdapter = new ChatsAdapter(chatClickListener);
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

    @Override
    public void onFragmentShow() {
        presenter.updateDialogs();
    }

    @Override
    public void showLoadSpinner(boolean show) {
        onLoadDialogsText.setVisibility(View.GONE);
        onLoadDialogsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setChats(Collection<DialogWidthMessagesLink> dialogsCollection) {
        chatsAdapter.clearAllChats();

        chatsAdapter.setChats(dialogsCollection);
    }

    @Override
    public void addOrUpdateChat(DialogWidthMessagesLink chat) {
        chatsAdapter.setOrUpdateChat(chat);
    }

    @Override
    public void addChat(DialogWidthMessagesLink chat) {
        chatsAdapter.addChat(chat);
    }

    @Override
    public void clearChats() {
        chatsAdapter.clearAllChats();
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
    }
}
