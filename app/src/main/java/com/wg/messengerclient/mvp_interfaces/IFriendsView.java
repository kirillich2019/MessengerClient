package com.wg.messengerclient.mvp_interfaces;

import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;

import java.util.Collection;

public interface IFriendsView extends IShowError {
    void showAllInFriendsRequests(Collection<FriendRequest> requests);

    void deleteRequest(int position);

    void showFriendRequestsLabel();

    void hideFriendRequestsLabel();
}
