package com.example.bas_bk.dstunotify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class MessageActivity extends AppCompatActivity {
SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        preferences = getSharedPreferences("MarkedMessage", MODE_PRIVATE);
        Intent intent = getIntent();
        Message message = intent.getParcelableExtra("Message");
        setTitle(message.getTheme());
        TextView sender = (TextView) findViewById(R.id.senderFull);
        TextView time = (TextView) findViewById(R.id.timeFull);
        TextView messageFull = (TextView)findViewById(R.id.messageFull);
        assert sender != null;
        sender.setText(message.getSender());
        assert time != null;
        time.setText(message.getTime());
        assert messageFull != null;
        messageFull.setText(message.getText());
        if (!message.isWatched()){
            Long id = message.getRemoteId();
            String s = null;
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(preferences.getString("jsonArray", "[]"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(id);
            NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
            networkAsyncTask.execute("MarkMessage", jsonArray.toString() , LoginActivity.LOGIN, LoginActivity.PASS);
            try {
                s = networkAsyncTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (s.equals("off")){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("jsonArray", jsonArray.toString());
                editor.apply();
            }
        }
    }
}
