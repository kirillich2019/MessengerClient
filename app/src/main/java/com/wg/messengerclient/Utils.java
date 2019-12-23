package com.wg.messengerclient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class Utils {

    public static <T> T await(Observable<T> observable) {
        CountDownLatch semaphore = new CountDownLatch(1);
        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        observable
                .subscribeOn(Schedulers.io())
                .subscribe(r -> {
                    result.set(r);
                    semaphore.countDown();
                }, e -> {
                    error.set(e);
                    semaphore.countDown();
                });

        try {
            semaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (error.get() != null) throw new RuntimeException(error.get());

        return result.get();
    }
}
