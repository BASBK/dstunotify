package com.example.bas_bk.dstunotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    RealmResults<Message> realmMessages;
    public static ArrayList<Message> realmList;
    public static RealmMessageAdapter realmAdapter;
    public static Realm realm;
    public static RecyclerView rvMessages;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        rvMessages = (RecyclerView) findViewById(R.id.msgList);
        realmMessages = realm.where(Message.class).findAll();
        realmList = new ArrayList<>();
        for (int i = realmMessages.size()-1; i >= 0 ; i--){
            realmList.add(realmMessages.get(i));
        }
        realmAdapter = new RealmMessageAdapter(this, realmList);
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
        if (!jsonString.isEmpty() && !jsonString.equals("[]") && !jsonString.equals("null") && !jsonString.equals("off")) {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONArray IDs = new JSONArray();
            for (int i = jsonArray.length()-1; i >= 0; i--) {
                IDs.put(jsonArray.getJSONObject(i).getInt("Id"));
                realm.beginTransaction();
                Message msg = new Message(jsonArray.getJSONObject(i).getInt("Id"), jsonArray.getJSONObject(i).getString("TextMessage"),
                        jsonArray.getJSONObject(i).getString("Sender"),
                        jsonArray.getJSONObject(i).getString("Theme"),
                        jsonArray.getJSONObject(i).getString("Date"), jsonArray.getJSONObject(i).getBoolean("IsWatched"));
                realm.copyToRealm(msg);
                realm.commitTransaction();
                realmList.add(0, msg);
            }
            realmAdapter.notifyItemRangeInserted(0, jsonArray.length());
            rvMessages.smoothScrollToPosition(0);
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

