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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.wg.messengerclient.R;
import com.wg.messengerclient.adapter.FriendsRequestsAdapter;
import com.wg.messengerclient.adapter.MyFriendsAdapter;
import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.interfaces.IOpenUserProfile;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendInfo;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;
import com.wg.messengerclient.mvp_interfaces.IFriendsView;
import com.wg.messengerclient.presenters.FriendsPresenter;

import java.util.Arrays;
import java.util.Collection;

public class FriendsFragment extends Fragment implements IFriendsView {
    private IOpenUserProfile openUserProfile;
    private RecyclerView friendsRequestsRecyclerView;
    private RecyclerView myFriendsRecyclerView;
    private FriendsRequestsAdapter friendsRequestsAdapter;
    private MyFriendsAdapter myFriendsAdapter;
    private FriendsPresenter presenter;
    private CardView friendsRequestLabel;
    private TextInputEditText wantedFriendLogin;
    private Button sendRequestButton;
    private TextView searchResult;
    private TextView noFriends;

    public FriendsFragment(IOpenUserProfile openUserProfile) {
        this.openUserProfile = openUserProfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noFriends = view.findViewById(R.id.no_friends_textView);

        friendsRequestsRecyclerView = view.findViewById(R.id.friends_requests_res_view);
        friendsRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myFriendsRecyclerView = view.findViewById(R.id.my_friends_res_view);
        myFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        friendsRequestLabel = view.findViewById(R.id.friends_req_label);
        wantedFriendLogin = view.findViewById(R.id.search_textEdit);

        sendRequestButton = view.findViewById(R.id.search_friends);
        sendRequestButton.setOnClickListener(v -> presenter.tryFindFriendsAndThrowRequest(wantedFriendLogin.getText().toString()));

        searchResult = view.findViewById(R.id.search_result);

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

        friendsRequestsAdapter = new FriendsRequestsAdapter(onRequestClickListener);
        friendsRequestsRecyclerView.setAdapter(friendsRequestsAdapter);

        MyFriendsAdapter.OnFriendClickListener onFriendClickListener = new MyFriendsAdapter.OnFriendClickListener() {
            @Override
            public void onMessageButtonClick(int position) {

            }

            @Override
            public void onProfileButtonClick(int position) {
                presenter.getFullProfileInfoAndTryOpenUserProfile(position);
            }
        };

        myFriendsAdapter = new MyFriendsAdapter(onFriendClickListener);
        myFriendsRecyclerView.setAdapter(myFriendsAdapter);
    }

    @Override
    public void showError(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAllInFriendsRequests(Collection<FriendRequest> requests) {
        friendsRequestsAdapter.clearAllRequest();
        friendsRequestsAdapter.setRequests(requests);
    }

    @Override
    public void showAllFriends(Collection<FriendInfo> friendInfoCollection) {
        myFriendsAdapter.clearAllFriend();
        myFriendsAdapter.setFriends(friendInfoCollection);

        noFriends.setVisibility(friendInfoCollection.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void clearFriendsList() {
        myFriendsAdapter.clearAllFriend();
    }

    @Override
    public void deleteRequest(int position) {
        friendsRequestsAdapter.deleteRequest(position);
    }

    @Override
    public void showFriendRequestsLabel() {
        friendsRequestLabel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFriendRequestsLabel() {
        friendsRequestLabel.setVisibility(View.GONE);
    }

    @Override
    public void setFriendsSearchButtonActivity(Boolean activity) {
        sendRequestButton.setEnabled(activity);
    }

    @Override
    public void setSearchResultMessage(String message) {
        searchResult.setText(message);
    }

    @Override
    public void openUserProfiel(FullProfileInfo profileInfo) {
        openUserProfile.open(profileInfo);
    }
}
