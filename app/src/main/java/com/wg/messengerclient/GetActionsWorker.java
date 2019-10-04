package com.wg.messengerclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.models.server_answers.Action;
import com.wg.messengerclient.models.server_answers.ActionType;
import com.wg.messengerclient.models.server_answers.ActionsAnswer;
import com.wg.messengerclient.presenters.CacheKeeper;
import com.wg.messengerclient.server.LongOperationMessengerServerApi;
import com.wg.messengerclient.server.Server;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.BlockingObservableNext;
import io.reactivex.schedulers.Schedulers;

public class GetActionsWorker extends Worker {
    public static final String TAG = "ajshfjksaehfsa";
    private LongOperationMessengerServerApi server = Server.getInstanceLongOperationsServer();

    public GetActionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        CacheKeeper cacheKeeper = new CacheKeeper();

        cacheKeeper.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    ActionsAnswer response = server.getActions(user.getToken(),1).execute().body();

                    for (Action action : response.getActions()) {
                        switch (action.getType()) {
                            case ActionType.FRIEND_REQUEST_RECEIVED:
                                Log.d("Poshel nahui", String.valueOf(action.getId()));
                                break;
                        }
                    }
                }, error -> {

                });


        return Result.success();
    }


    public static void startWorker(Context context) {
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(GetActionsWorker.class)
                .addTag(TAG)
                .build();

        WorkManager.getInstance(context).enqueue(myWorkRequest);
    }

    public static void stopWorker(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG);
    }
}
