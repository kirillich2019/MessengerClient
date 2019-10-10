package com.wg.messengerclient.long_pol_actions;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.models.server_answers.Action;
import com.wg.messengerclient.models.server_answers.ActionsAnswer;
import com.wg.messengerclient.presenters.CacheKeeper;
import com.wg.messengerclient.server.LongOperationMessengerServerApi;
import com.wg.messengerclient.server.Server;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;

public class GetActionsWorker extends Worker {
    public static final String TAG = "NOTIFICATION";
    private LongOperationMessengerServerApi server = Server.getInstanceLongOperationsServer();
    BaseProfileInfoDao baseProfileInfoDao = SingletonDatabase.getDatabaseInstance().baseProfileInfoDao();
    CacheKeeper cacheKeeper = new CacheKeeper();

    public GetActionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private void saveLastActionsIdInDb(int lastActionsId) {
        BaseProfileInfo currentUser = baseProfileInfoDao.getFirstOrNull();
        currentUser.setLastActionsId(lastActionsId);
        baseProfileInfoDao.update(currentUser);
    }

    public static void startWorker(Context context) {
        PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(GetActionsWorker.class, 1, TimeUnit.MINUTES)
                .addTag(TAG)
                .build();

        WorkManager.getInstance(context).enqueue(myWorkRequest);
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        cacheKeeper.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    ActionsAnswer response = server.getActions(user.getToken(), user.getLastActionsId()).execute().body();

                    for (Action action : response.getActions()) {
                        DistributeRxEventBus.GetInstance().send(action);
                    }

                    if (!response.getActions().isEmpty()) {
                        saveLastActionsIdInDb(response.getActions().get(response.getActions().size() - 1).getId());
                    }
                }, error -> {

                });

        return Result.success();
    }

    public static void stopWorker(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG);
    }
}
