package com.wg.messengerclient.long_pol_actions;

import com.wg.messengerclient.models.server_answers.Action;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DistributeRxEventBus {
    private static DistributeRxEventBus distributeRxEvetBus;

    public static DistributeRxEventBus GetInstance(){
        if(distributeRxEvetBus == null)
            distributeRxEvetBus = new DistributeRxEventBus();

        return distributeRxEvetBus;
    }

    private DistributeRxEventBus(){}

    private PublishSubject<Action> bus = PublishSubject.create();

    public void send(Action action) {
        bus.onNext(action);
    }

    public Observable<Action> toObservable() {
        return bus;
    }
}
