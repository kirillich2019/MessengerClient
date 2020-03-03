package com.wg.messengerclient.activity_and_fargments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wg.messengerclient.R;
import com.wg.messengerclient.adapter.ChatsAdapter;
import com.wg.messengerclient.models.server_answers.internalEntities.Chat;
import com.wg.messengerclient.mvp_interfaces.IChatsView;
import com.wg.messengerclient.presenters.ChatsPresenter;

public class ChatFragment extends Fragment implements IChatsView {
    private RecyclerView chatsResyclerView;
    private ChatsAdapter chatsAdapter;
    private ChatsPresenter presenter;

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

        chatsResyclerView = view.findViewById(R.id.my_chats_res_view);
        chatsResyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter = new ChatsPresenter(this);
        getLifecycle().addObserver(presenter);

        ChatsAdapter.OnChatClickListener chatClickListener = new ChatsAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(int position) {
                showError("test");
            }
        };

        chatsAdapter = new ChatsAdapter(chatClickListener);
        chatsResyclerView.setAdapter(chatsAdapter);
    }

    @Override
    public void OnFragmentShow() {
        chatsAdapter.addChat(new Chat(0, "login1", "a"));
        chatsAdapter.addChat(new Chat(1, "login2", "a"));
        chatsAdapter.addChat(new Chat(2, "login3", "a"));
        chatsAdapter.addChat(new Chat(3, "login4", "a"));
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
    }
}
