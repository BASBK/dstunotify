package com.example.bas_bk.dstunotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainActivity extends AppCompatActivity {
    RealmResults<Message> realmMessages;
    public static RealmMessageAdapter realmAdapter;
    public static Realm realm;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        RecyclerView rvMessages = (RecyclerView) findViewById(R.id.msgList);
        realmMessages = realm.where(Message.class).findAll();
        realmAdapter = new RealmMessageAdapter(this, realmMessages);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(200);
        animator.setRemoveDuration(200);
        assert rvMessages != null;
        rvMessages.setItemAnimator(animator);
        rvMessages.setAdapter(realmAdapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        if (realm.where(Message.class).findAll().size() == 0){
            NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
            networkAsyncTask.execute("GetMessages", LoginActivity.LOGIN, LoginActivity.PASS, "0");
            try {
                Save2LocalBase(networkAsyncTask.get());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void onAddBtnClick(View view) throws ExecutionException, InterruptedException, JSONException {
//        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
//        networkAsyncTask.execute("GetMessages", LoginActivity.LOGIN, LoginActivity.PASS, "1");
//        Save2LocalBase(networkAsyncTask.get());
        stopService(i);

    }

    public static void Save2LocalBase(String jsonString) throws JSONException {
        if (!jsonString.isEmpty() && !jsonString.equals("[]") && !jsonString.equals("null")) {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONArray IDs = new JSONArray();
            Integer curSize = realmAdapter.getItemCount();
            for (int i = 0; i < jsonArray.length(); i++) {
                IDs.put(jsonArray.getJSONObject(i).getInt("Id"));
                realm.beginTransaction();
                Message msg = new Message(jsonArray.getJSONObject(i).getInt("Id"), jsonArray.getJSONObject(i).getString("TextMessage"),
                        jsonArray.getJSONObject(i).getString("Sender"),
                        jsonArray.getJSONObject(i).getString("Theme"),
                        jsonArray.getJSONObject(i).getString("Date"), jsonArray.getJSONObject(i).getBoolean("IsWatched"));
                realm.copyToRealm(msg);
                realm.commitTransaction();
            }
            realmAdapter.notifyItemRangeInserted(0, jsonArray.length());
            NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
            networkAsyncTask.execute("VerifyMessages", IDs.toString(), LoginActivity.LOGIN, LoginActivity.PASS);
        }
    }

    public void onDelBtnClick(View view) {
//        Integer fullDBSize = realmAdapter.getItemCount();
//        realm.beginTransaction();
//        realm.deleteAll();
//        realm.commitTransaction();
//        realmAdapter.notifyItemRangeRemoved(0, fullDBSize);
        i = new Intent(this, NetworkService.class);
        startService(i);
    }

    public void onItemClick(View view){

    }
}

