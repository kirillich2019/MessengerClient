package com.wg.messengerclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wg.messengerclient.R;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendsRequestsAdapter extends RecyclerView.Adapter<FriendsRequestsAdapter.FriendsRequestsHolder> {
    private OnRequestClickListener onRequestClickListner;
    private List<FriendRequest> allRequests = new ArrayList<>();

    private TextView login;
    private ImageView ava;

    public FriendsRequestsAdapter(OnRequestClickListener onRequestClickListner) {
        this.onRequestClickListner = onRequestClickListner;
    }

    @NonNull
    @Override
    public FriendsRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_request_view, parent, false);
        return new FriendsRequestsHolder(view);
    }

    public void addRequest(FriendRequest newRequest){
        allRequests.add(newRequest);
        notifyDataSetChanged();
    }

    public void deleteRequest(int position){
        allRequests.remove(position);
        notifyDataSetChanged();
    }

    public void clearAllRequest(){
        allRequests.clear();
        notifyDataSetChanged();
    }

    public void setRequests(Collection<FriendRequest> requests){
        allRequests.addAll(requests);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRequestsHolder holder, int position) {
        holder.bind(allRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return allRequests.size();
    }

    class FriendsRequestsHolder extends RecyclerView.ViewHolder {
        private TextView login;
        private ImageView ava;
        private Button accept;
        private ImageButton noAccept;
        private Context context;

        public FriendsRequestsHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            login = itemView.findViewById(R.id.friend_req_login);
            ava = itemView.findViewById(R.id.friend_ava);
            accept = itemView.findViewById(R.id.accept_request);
            noAccept = itemView.findViewById(R.id.no_accept_request);

            accept.setOnClickListener(v -> onRequestClickListner.onAcceptButtonClick(getLayoutPosition()));
            noAccept.setOnClickListener(v -> onRequestClickListner.onNotAcceptButtonClick(getLayoutPosition()));
        }

        public void bind(FriendRequest request){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(request.getAvaUrl())
                    .into(ava);

            login.setText("@:" + request.getLogin());
        }
    }

    public interface OnRequestClickListener {
        void onAcceptButtonClick(int position);

        void onNotAcceptButtonClick(int position);
    }
}
