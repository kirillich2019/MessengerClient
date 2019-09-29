package com.wg.messengerclient.activity_and_fargments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wg.messengerclient.R;
import com.wg.messengerclient.adapter.FriendsRequestsAdapter;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;
import com.wg.messengerclient.mvp_interfaces.IFriendsView;
import com.wg.messengerclient.presenters.FriendsPresenter;

import java.util.Arrays;
import java.util.Collection;

public class FriendsFragment extends Fragment implements IFriendsView {
    private RecyclerView friendsRequestsRecyclerView;
    private FriendsRequestsAdapter adapter;
    private FriendsPresenter presenter;
    private CardView friendsRequestLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendsRequestsRecyclerView = view.findViewById(R.id.friends_requests_res_view);
        friendsRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsRequestLabel = view.findViewById(R.id.friends_req_label);

        presenter = new FriendsPresenter(this);
        getLifecycle().addObserver(presenter);

        FriendsRequestsAdapter.OnRequestClickListener onRequestClickListener = new FriendsRequestsAdapter.OnRequestClickListener() {
            @Override
            public void onAcceptButtonClick(int position) {
                presenter.tryAcceptInRequest(position);
            }

            @Override
            public void onNotAcceptButtonClick(int position) {
                presenter.tryNotAcceptInRequest(position);
            }
        };

        adapter = new FriendsRequestsAdapter(onRequestClickListener);
        friendsRequestsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAllInFriendsRequests(Collection<FriendRequest> requests) {
        adapter.clearAllRequest();
        adapter.setRequests(requests);
    }

    @Override
    public void deleteRequest(int position) {
        adapter.deleteRequest(position);
    }

    @Override
    public void showFriendRequestsLabel() {
        friendsRequestLabel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFriendRequestsLabel() {
        friendsRequestLabel.setVisibility(View.GONE);
    }
}
