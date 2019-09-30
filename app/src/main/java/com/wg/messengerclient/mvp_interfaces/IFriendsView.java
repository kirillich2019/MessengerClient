package com.wg.messengerclient.mvp_interfaces;

import com.wg.messengerclient.database.entities.FullProfileInfo;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendInfo;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;

import java.util.Collection;

public interface IFriendsView extends IShowError {
    void showAllInFriendsRequests(Collection<FriendRequest> requests);

    void showAllFriends(Collection<FriendInfo> friendInfoCollection);

    void clearFriendsList();

    void deleteRequest(int position);

    void showFriendRequestsLabel();

    void hideFriendRequestsLabel();

    void setFriendsSearchButtonActivity(Boolean activity);

    void setSearchResultMessage(String message);

    void openUserProfiel(FullProfileInfo profileInfo);
}
