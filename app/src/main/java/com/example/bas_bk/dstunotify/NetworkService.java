package com.example.bas_bk.dstunotify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.LogRecord;

/**
 * Created by BAS_BK on 03.09.2016.
 */
public class NetworkService extends Service {
    NetworkAsyncTask networkAsyncTask;
    SharedPreferences preferences;
    String LOGIN;
    String PASS;
    Timer timer;
    TimerTask timerTask;
    NotificationManager nm;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

        preferences = getSharedPreferences("Account", MODE_PRIVATE);
        LOGIN = preferences.getString("LOGIN", "");
        PASS = preferences.getString("PASS", "");
        timer = new Timer();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (timerTask != null) timerTask.cancel();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Run();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 2000, 10000);
        return START_STICKY;
    }

    public void Run() throws ExecutionException, InterruptedException {
        networkAsyncTask = new NetworkAsyncTask();
        networkAsyncTask.execute("GetMessages", LOGIN, PASS, "1");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    String r = networkAsyncTask.get();
                    if (r != null) {
                        MainActivity.Save2LocalBase(r);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");
//
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        mBuilder.setContentIntent(pIntent);
//        mBuilder.mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        nm.notify(1, mBuilder.build());
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
    }
}
