package com.wg.messengerclient.long_pol_actions;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.wg.messengerclient.R;
import com.wg.messengerclient.Utils;
import com.wg.messengerclient.activity_and_fargments.LoginActivity;
import com.wg.messengerclient.database.SingletonDatabase;
import com.wg.messengerclient.database.dao.BaseProfileInfoDao;
import com.wg.messengerclient.database.entities.BaseProfileInfo;
import com.wg.messengerclient.models.server_answers.Action;
import com.wg.messengerclient.models.server_answers.ActionType;
import com.wg.messengerclient.models.server_answers.ActionsAnswer;
import com.wg.messengerclient.presenters.CacheKeeper;
import com.wg.messengerclient.server.LongOperationMessengerServerApi;
import com.wg.messengerclient.server.Server;

import java.io.IOException;
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
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(GetActionsWorker.class)
                .addTag(TAG)
                .build();

        WorkManager.getInstance(context).enqueue(myWorkRequest);
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "started");
        while (!isStopped()) {
            try {
                BaseProfileInfo user = Utils.await(cacheKeeper.getCurrentUser());
                Log.d(TAG, "waiting");
                ActionsAnswer response = server.getActions(user.getToken(), user.getLastActionsId()).execute().body();
                Log.d(TAG, "done");

                for (Action action : response.getActions()) {
                    DistributeRxEventBus.GetInstance().send(action);

                    switch (action.getType()) {
                        case ActionType.FRIEND_REQUEST_RECEIVED:
                            showNotification(getApplicationContext(), "Новый запрос в друзья.", String.valueOf(action.getId()));
                            break;
                        case ActionType.FRIEND_REQUEST_ACCEPTED:
                            showNotification(getApplicationContext(), "Запрос в друзья одобрен.", String.valueOf(action.getId()));
                            break;
                        case ActionType.MESSAGE:
                            showNotification(getApplicationContext(), "Новое сообщение.", String.valueOf(action.getId()));
                            break;
                    }
                }

                if (!response.getActions().isEmpty()) {
                    saveLastActionsIdInDb(response.getActions().get(response.getActions().size() - 1).getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "finished");

        return Result.success();
    }

    private static final int NOTIFY_ID = 199017283;
    private static final String CHANNEL_ID = TAG;

    private void showNotification(
            Context context,
            String title,
            String body
    ) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    CHANNEL_ID, context.getString(
                    R.string.app_name
            ), NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(body);

        Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
        resultIntent.putExtra("openFriendsFragment", true);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext()
                , 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,
                CHANNEL_ID
        )
                .setSmallIcon(R.drawable.logo1)
                .setStyle(bigTextStyle)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);


        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    public static void stopWorker(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG);
    }
}
