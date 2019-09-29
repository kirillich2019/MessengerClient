package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;
import com.wg.messengerclient.mvp_interfaces.IFriendsView;
import com.wg.messengerclient.server.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FriendsPresenter extends CacheKeeper implements LifecycleObserver {
    private IFriendsView friendsView;
    private CompositeDisposable disposables = new CompositeDisposable();
    private List<FriendRequest> currentFriendsRequests = new ArrayList<>();

    public FriendsPresenter(IFriendsView friendsView) {
        this.friendsView = friendsView;

        loadAllInFriendRequestList();
    }

    @SuppressLint("CheckResult")
    private void loadAllInFriendRequestList() {
        disposables.add(
                getCurrentUserToken()
                        .flatMap(token -> Server.getInstanceShortOperationsServer().getAllFriendsRequests(token))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(friendsRequestsAnswer -> {
                            friendsView.hideFriendRequestsLabel();

                            if (friendsRequestsAnswer.getError() != 0) {
                                friendsView.showError(friendsRequestsAnswer.getErrorText() != null ?
                                        friendsRequestsAnswer.getErrorText() :
                                        Integer.toString(friendsRequestsAnswer.getError()));
                                return;
                            }

                            if (friendsRequestsAnswer.getIds().size() == 0)
                                return;

                            friendsView.showFriendRequestsLabel();

                            Observable.fromIterable(friendsRequestsAnswer.getIds())
                                    .flatMap(id -> Server.getInstanceShortOperationsServer().getPublicProfileInfo(id),
                                            (id, answer) -> {
                                                answer.setId(id);
                                                return answer;
                                            })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(answer -> {
                                                if (answer.getError() == 0) {
                                                    currentFriendsRequests.add(
                                                            new FriendRequest(answer.getId(), answer.getLogin(), answer.getAvatar())
                                                    );
                                                }
                                            }, error -> friendsView.showError("Ошибка загрузки запросов в друзья."),
                                            () -> friendsView.showAllInFriendsRequests(currentFriendsRequests));
                        }, error -> friendsView.showError(error.getMessage())));
    }

    @SuppressLint("CheckResult")
    public void tryAcceptInRequest(int position) {
        if (currentFriendsRequests.get(position) != null) {
            FriendRequest request = currentFriendsRequests.get(position);

            getCurrentUserToken().
                    flatMap(token -> Server.getInstanceShortOperationsServer().responseToRequest(token, request.getId(), 1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(answerWithError -> {
                        if (answerWithError.getError() != 0) {
                            friendsView.showError(answerWithError.getErrorText() != null ?
                                    answerWithError.getErrorText() :
                                    Integer.toString(answerWithError.getError()));
                            return;
                        }

                        currentFriendsRequests.remove(request);
                        friendsView.deleteRequest(position);
                        friendsView.showError("Заявка принята.");
                    }, error -> friendsView.showError("Ошибка принятия запроса в друзья."));
        }
    }

    @SuppressLint("CheckResult")
    public void tryNotAcceptInRequest(int position) {
        if (currentFriendsRequests.get(position) != null) {
            FriendRequest request = currentFriendsRequests.get(position);

            getCurrentUserToken().
                    flatMap(token -> Server.getInstanceShortOperationsServer().responseToRequest(token, request.getId(), 0))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(answerWithError -> {
                        if (answerWithError.getError() != 0) {
                            friendsView.showError(answerWithError.getErrorText() != null ?
                                    answerWithError.getErrorText() :
                                    Integer.toString(answerWithError.getError()));
                            return;
                        }

                        currentFriendsRequests.remove(request);
                        friendsView.deleteRequest(position);
                        friendsView.showError("Заявка отклонена.");
                    }, error -> friendsView.showError("Ошибка отклонения запроса в друзья."));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        disposables.dispose();
    }
}
