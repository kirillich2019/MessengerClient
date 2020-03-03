package com.wg.messengerclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wg.messengerclient.R;
import com.wg.messengerclient.models.server_answers.internalEntities.Chat;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsHolder>{
    private ChatsAdapter.OnChatClickListener onChatClickListner;
    private List<Chat> allChats = new ArrayList<>();

    public ChatsAdapter(ChatsAdapter.OnChatClickListener onChatClickListner) {
        this.onChatClickListner = onChatClickListner;
    }

    @NonNull
    @Override
    public ChatsAdapter.ChatsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_chat_view, parent, false);
        return new ChatsAdapter.ChatsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsHolder holder, int position) {
        holder.bind(allChats.get(position));
    }

    public void addChat(Chat newChat){
        allChats.add(newChat);
        notifyDataSetChanged();
    }

    public void deleteChat(int position){
        allChats.remove(position);
        notifyDataSetChanged();
    }

    public void clearAllChats(){
        allChats.clear();
        notifyDataSetChanged();
    }

    public void setChat(Collection<Chat> chats){
        allChats.addAll(chats);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return allChats.size();
    }

    class ChatsHolder extends RecyclerView.ViewHolder {
        private TextView lastMsg;
        private ImageView ava;
        private ConstraintLayout layout;
        private Context context;

        public ChatsHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            layout = itemView.findViewById(R.id.chat_layout);
            lastMsg = itemView.findViewById(R.id.last_msg);
            ava = itemView.findViewById(R.id.dialog_ava);

            layout.setOnClickListener(v -> onChatClickListner.onChatClick(getLayoutPosition()));
        }

        public void bind(Chat request){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(request.getAvaUrl())
                    .into(ava);

            lastMsg.setText("@:" + request.getLogin());
        }
    }

    public interface OnChatClickListener {
        void onChatClick(int position);
    }
}
