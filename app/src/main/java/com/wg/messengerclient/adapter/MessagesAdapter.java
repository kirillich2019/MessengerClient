package com.wg.messengerclient.adapter;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.RequestOptions;
import com.wg.messengerclient.R;
import com.wg.messengerclient.database.entities.MessageDbEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {
    public List<MessageDbEntity> messages = new ArrayList<>();

    private int currentUserId;

    public MessagesAdapter(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessagesAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout, parent, false);
        return new MessagesAdapter.MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MessageHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    public void addAllMsg(MessageDbEntity newMsg){
        this.messages.add(newMsg);
        notifyDataSetChanged();
    }

    public void clearAllMsg(){
        messages.clear();
        notifyDataSetChanged();
    }

    public void addAllMsg(Collection<MessageDbEntity> messages){
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void setAllMsg(Collection<MessageDbEntity> messages){
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private TextView messageText;
        private CardView messageCardView;

        public MessageHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            messageText = itemView.findViewById(R.id.message_text);
            messageCardView = itemView.findViewById(R.id.message_cardView);
        }

        public void bind(MessageDbEntity msg) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);
            messageText.setText(msg.getMessage().getText());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(messageCardView.getLayoutParams());

            if(msg.getMessage().getSender() == currentUserId)
                params.gravity = Gravity.END;
            else
                params.gravity = Gravity.START;

            messageCardView.setLayoutParams(params);
        }
    }
}
