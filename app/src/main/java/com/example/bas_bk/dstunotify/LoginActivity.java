package com.example.bas_bk.dstunotify;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    public static String LOGIN;
    public static String PASS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Вход");
        preferences = getSharedPreferences("Account" , MODE_PRIVATE);
        LOGIN = preferences.getString("LOGIN", "");
        PASS = preferences.getString("PASS", "");
        if (!TextUtils.isEmpty(LOGIN)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void SignIn(View view) throws ExecutionException, InterruptedException {
        EditText loginET = (EditText)findViewById(R.id.loginET);
        EditText passET = (EditText)findViewById(R.id.passET);
        LOGIN = loginET.getText().toString();
        PASS = passET.getText().toString();
        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
        networkAsyncTask.execute("SignIn", LOGIN, PASS);
        String s = networkAsyncTask.get();
        if (Objects.equals(s, "1")){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("LOGIN", LOGIN);
            editor.putString("PASS", PASS);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Ошибка авторизации. Введён неверный логин и/или пароль", Toast.LENGTH_LONG).show();
        }
    }
    public void toSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
