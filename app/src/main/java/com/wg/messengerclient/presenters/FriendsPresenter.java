package com.wg.messengerclient.presenters;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wg.messengerclient.models.server_answers.Errors;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendInfo;
import com.wg.messengerclient.models.server_answers.internalEntities.FriendRequest;
import com.wg.messengerclient.mvp_interfaces.IFriendsView;
import com.wg.messengerclient.server.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FriendsPresenter extends CacheKeeper implements LifecycleObserver {
    private IFriendsView friendsView;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Disposable loadFriendFromDbDisposable;
    private List<FriendRequest> currentFriendsRequests = new ArrayList<>();
    private List<FriendInfo> currentFriends = new ArrayList<>();

    public FriendsPresenter(IFriendsView friendsView) {
        this.friendsView = friendsView;

        loadAllInFriendRequestList();
        loadFriendsFromDB();
        loadFromTheInternetAndCacheAllFriend();
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

            disposables.add(
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

                                //todo заглушка
                                if (currentFriendsRequests.size() == 0)
                                    friendsView.hideFriendRequestsLabel();

                                loadFromTheInternetAndCacheAllFriend();
                            }, error -> friendsView.showError("Ошибка принятия запроса в друзья."))
            );
        }
    }

    @SuppressLint("CheckResult")
    public void tryNotAcceptInRequest(int position) {
        if (currentFriendsRequests.get(position) != null) {
            FriendRequest request = currentFriendsRequests.get(position);

            disposables.add(
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

                                //todo заглушка
                                if (currentFriendsRequests.size() == 0)
                                    friendsView.hideFriendRequestsLabel();

                                loadFromTheInternetAndCacheAllFriend();
                            }, error -> friendsView.showError("Ошибка отклонения запроса в друзья."))
            );
        }
    }

    @SuppressLint("CheckResult")
    public void tryFindFriendsAndThrowRequest(String login) {
        login = login.trim();

        if (login.isEmpty())
            return;

        String finalLogin = login;

        friendsView.setFriendsSearchButtonActivity(false);

        getCurrentUserToken().
                flatMap(token -> Server.getInstanceShortOperationsServer().sendFriendRequest(token, finalLogin))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(answerWithError -> {
                    friendsView.setFriendsSearchButtonActivity(true);

                    if (answerWithError.getError() != 0) {
                        Errors errors = Errors.getErrorByCode(answerWithError.getError());
                        if (errors != null) {
                            friendsView.setSearchResultMessage(errors.getMessage());
                            return;
                        }

                        friendsView.setSearchResultMessage(answerWithError.getErrorText() != null ?
                                answerWithError.getErrorText()
                                : "Пользователь не найден");
                    }

                    friendsView.setSearchResultMessage("Запрос успешно отправлен");
                }, error -> {
                    friendsView.setFriendsSearchButtonActivity(true);
                    friendsView.setSearchResultMessage("Ошибка поиска, проверьте ваше подключение к сети.");
                });
    }

    @SuppressLint("CheckResult")
    public void loadFromTheInternetAndCacheAllFriend() {
        disposables.add(
                getCurrentUserToken()
                        .flatMap(token -> Server.getInstanceShortOperationsServer().getAllFriends(token))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(friendsIDsAnswer -> {
                            if (friendsIDsAnswer.getError() != 0) {
                                friendsView.showError(friendsIDsAnswer.getErrorText() != null ?
                                        friendsIDsAnswer.getErrorText() :
                                        Integer.toString(friendsIDsAnswer.getError()));
                                return;
                            }

                            if (loadFriendFromDbDisposable != null)
                                loadFriendFromDbDisposable.dispose();

                            currentFriends.clear();

                            if (friendsIDsAnswer.getIds().size() == 0) {
                                friendsView.clearFriendsList();
                                return;
                            }

                            Observable.fromIterable(friendsIDsAnswer.getIds())
                                    .flatMap(id -> Server.getInstanceShortOperationsServer().getPublicProfileInfo(id),
                                            (id, answer) -> {
                                                answer.setId(id);
                                                return answer;
                                            })
                                    .flatMap(profileInfoAnswer -> cacheFriendUser(profileInfoAnswer))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(answer -> {
                                                if (answer.getError() == 0) {
                                                    currentFriends.add(
                                                            new FriendInfo(
                                                                    answer.getId(),
                                                                    answer.getLogin(),
                                                                    answer.getAvatar(),
                                                                    answer.getName(),
                                                                    answer.getSurname()
                                                            )
                                                    );
                                                }
                                            }, error -> friendsView.showError("Ошибка загрузки друзей."),
                                            () -> friendsView.showAllFriends(currentFriends));
                        }, error -> friendsView.showError(error.getMessage())));
    }

    private void loadFriendsFromDB() {
        loadFriendFromDbDisposable =
                getCacheFrieds()
                        .flatMap(friendIds -> Observable.fromIterable(friendIds))
                        .flatMap(friendId -> getFullProfileInfoById(friendId.getId()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(profileInfo -> {
                                    if (profileInfo.getIndex() == 0) {
                                        currentFriends.add(
                                                new FriendInfo(
                                                        0,
                                                        "unknown",
                                                        profileInfo.getAvatarUrl(),
                                                        "unknown",
                                                        ""
                                                )
                                        );

                                        return;
                                    }

                                    currentFriends.add(
                                            new FriendInfo(
                                                    profileInfo.getId(),
                                                    profileInfo.getLogin(),
                                                    profileInfo.getAvatarUrl(),
                                                    profileInfo.getName(),
                                                    profileInfo.getSurname()
                                            )
                                    );
                                }, error -> {
                                }
                                , () -> friendsView.showAllFriends(currentFriends));

    }

    @SuppressLint("CheckResult")
    public void getFullProfileInfoAndTryOpenUserProfile(int position) {
        if (currentFriends.get(position) != null) {
            getFullProfileInfoById(currentFriends.get(position).getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            profileInfo -> friendsView.openUserProfiel(profileInfo),
                            error -> friendsView.showError("Ошибка открытия профиля.")
                    );
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        disposables.dispose();
    }
}
