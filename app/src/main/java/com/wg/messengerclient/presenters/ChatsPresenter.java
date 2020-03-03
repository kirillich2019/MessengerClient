package com.wg.messengerclient.presenters;

import androidx.lifecycle.LifecycleObserver;

import com.wg.messengerclient.mvp_interfaces.IChatsView;
import io.reactivex.disposables.CompositeDisposable;

/*
Презентор списка диалогов
 */
public class ChatsPresenter implements LifecycleObserver {
    private IChatsView chatsView;
    private CompositeDisposable disposables = new CompositeDisposable();

    public ChatsPresenter(IChatsView chatsView){
        this.chatsView = chatsView;
    }
}
