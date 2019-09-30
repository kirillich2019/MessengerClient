package com.wg.messengerclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wg.messengerclient.R;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.FriendsHolder> {
    private OnFriendClickListener onFriendClickListener;
    private List<FriendInfo> allFriends = new ArrayList<>();

    public MyFriendsAdapter(OnFriendClickListener onRequestClickListner) {
        this.onFriendClickListener = onRequestClickListner;
    }

    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_friend_view, parent, false);
        return new FriendsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsHolder holder, int position) {
        holder.bind(allFriends.get(position));
    }

    public void addFriend(FriendInfo newRequest){
        allFriends.add(newRequest);
        notifyDataSetChanged();
    }

    public void deleteFriend(int position){
        allFriends.remove(position);
        notifyDataSetChanged();
    }

    public void clearAllFriend(){
        allFriends.clear();
        notifyDataSetChanged();
    }

    public void setFriends(Collection<FriendInfo> friendInfos){
        allFriends.addAll(friendInfos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return allFriends.size();
    }

    class FriendsHolder extends RecyclerView.ViewHolder {
        private TextView login, name;
        private ImageView ava;
        private ImageButton openDialog;
        private ImageButton openProfile;
        private Context context;

        public FriendsHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            name = itemView.findViewById(R.id.friend_name);
            login = itemView.findViewById(R.id.friend_login);
            ava = itemView.findViewById(R.id.friend_ava);

            openDialog = itemView.findViewById(R.id.open_dialog);
            openProfile = itemView.findViewById(R.id.open_profile);

            openDialog.setOnClickListener(v -> onFriendClickListener.onMessageButtonClick(getLayoutPosition()));
            openProfile.setOnClickListener(v -> onFriendClickListener.onProfileButtonClick(getLayoutPosition()));
        }

        public void bind(FriendInfo friendInfo){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(friendInfo.getAvaUrl())
                    .into(ava);

            login.setText("@:" + friendInfo.getLogin());
            name.setText(friendInfo.getName() + " " + friendInfo.getSurname());
        }
    }

    public interface OnFriendClickListener {
        void onMessageButtonClick(int position);

        void onProfileButtonClick(int position);
    }
}
