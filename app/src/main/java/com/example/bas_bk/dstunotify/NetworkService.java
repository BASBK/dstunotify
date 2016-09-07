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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.LogRecord;

import io.realm.Realm;

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
    public static Integer unReadNots;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        unReadNots = 0;
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 2000, 10000);
        return START_STICKY;
    }

    public void Run() throws ExecutionException, InterruptedException, JSONException {
        networkAsyncTask = new NetworkAsyncTask();
        networkAsyncTask.execute("GetMessages", LOGIN, PASS, "1");
        final String jsonString = networkAsyncTask.get();
        final JSONArray[] jsonArray = new JSONArray[1];
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Realm realm = Realm.getDefaultInstance();

                    if (!jsonString.isEmpty() && !jsonString.equals("[]") && !jsonString.equals("null") && !jsonString.equals("off")) {
                        jsonArray[0] = new JSONArray(jsonString);
                        JSONArray IDs = new JSONArray();
                        for (int i = jsonArray[0].length()-1; i >= 0; i--) {
                            IDs.put(jsonArray[0].getJSONObject(i).getInt("Id"));
                            realm.beginTransaction();
                            Message msg = new Message(jsonArray[0].getJSONObject(i).getInt("Id"), jsonArray[0].getJSONObject(i).getString("TextMessage"),
                                    jsonArray[0].getJSONObject(i).getString("Sender"),
                                    jsonArray[0].getJSONObject(i).getString("Theme"),
                                    jsonArray[0].getJSONObject(i).getString("Date"), jsonArray[0].getJSONObject(i).getBoolean("IsWatched"));
                            realm.copyToRealm(msg);
                            realm.commitTransaction();
                        }
                        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
                        networkAsyncTask.execute("VerifyMessages", IDs.toString(), LOGIN, PASS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (jsonArray[0] != null) {
            unReadNots += jsonArray[0].length();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("У вас новые сообщения! " + "(" + unReadNots + ")")
                            .setContentText("Нажмите, чтобы перейти к просмотру");

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            mBuilder.setContentIntent(pIntent);
            mBuilder.mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

            nm.notify(0, mBuilder.build());
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
    }
}
