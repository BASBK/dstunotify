package com.example.bas_bk.dstunotify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class SignUpActivity extends AppCompatActivity {
    EditText bookET;
    EditText phoneET;
    EditText emailET;
    EditText passET;
    EditText passConfirmET;
    CheckBox confirmCB;
    public static Spinner facultySpin;
    public static Spinner depSpin;
    public static Spinner groupSpin;
    ArrayAdapter<String> facultyAdapter;
    ArrayAdapter<String> departmentAdapter;
    ArrayAdapter<String> groupAdapter;
    SharedPreferences preferences;
    String[] faculties = new String[]{""};
    String[] departments = new String[]{""};
    String[] groups = new String[]{""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        facultySpin = (Spinner)findViewById(R.id.facultyS);
        depSpin = (Spinner)findViewById(R.id.departmentS);
        groupSpin = (Spinner)findViewById(R.id.groupS);
        bookET = (EditText)findViewById(R.id.bookET);
        phoneET = (EditText)findViewById(R.id.phoneET);
        emailET = (EditText)findViewById(R.id.emailET);
        passET = (EditText)findViewById(R.id.passET);
        passConfirmET = (EditText)findViewById(R.id.passConfirmET);
        confirmCB = (CheckBox)findViewById(R.id.confirmCB);

        final Gson gson = new Gson();

        departmentAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, departments);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentAdapter.setNotifyOnChange(true);
        depSpin.setAdapter(departmentAdapter);

        groupAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupAdapter.setNotifyOnChange(true);
        groupSpin.setAdapter(groupAdapter);

        facultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, faculties);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpin.setAdapter(facultyAdapter);

        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(this);
        networkAsyncTask.execute("GetFaculties");
        facultySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(SignUpActivity.this);
                networkAsyncTask.execute("GetDepartmentsFromFaculty", facultySpin.getSelectedItem().toString());
                depSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(SignUpActivity.this);
                        networkAsyncTask.execute("GetGroupsFromDepartment", depSpin.getSelectedItem().toString());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onSignUp(View view) throws IOException, ExecutionException, InterruptedException {
        if (check4Errors()){
            Gson gson = new Gson();
            NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask();
            StudentJson student = new StudentJson(
                    bookET.getText().toString(),
                    phoneET.getText().toString(),
                    emailET.getText().toString(),
                    passET.getText().toString(),
                    groupSpin.getSelectedItem().toString());

            networkAsyncTask.execute("SignUp", gson.toJson(student));
            if (Objects.equals(networkAsyncTask.get(), "1")){
                preferences = getSharedPreferences("Account" , MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LOGIN", bookET.getText().toString());
                editor.putString("PASS", passET.getText().toString());
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
    private boolean check4Errors(){
        if (TextUtils.isEmpty(bookET.getText().toString())){
            bookET.setError("Укажите номер зачётки!");
            return false;
        }
        if (TextUtils.isEmpty(phoneET.getText().toString())){
            phoneET.setError("Укажите номер телефона!");
            return false;
        }
        if (TextUtils.isEmpty(emailET.getText().toString())){
            emailET.setError("Укажите адрес электронной почты!");
            return false;
        }
        else if (!emailET.getText().toString().contains("@")){
            emailET.setError("Укажите корректный адрес электронной почты!");
            return false;
        }
        if (TextUtils.isEmpty(passET.getText().toString())){
            passET.setError("Укажите пароль!");
            return false;
        }
        if (TextUtils.isEmpty(passConfirmET.getText().toString())){
            passConfirmET.setError("Укажите пароль снова!");
            return false;
        }
        else if (!passET.getText().toString().equals(passConfirmET.getText().toString())){
            passConfirmET.setError("Пароли не совпадают!");
            return false;
        }
        if (!confirmCB.isChecked()){
            confirmCB.setError("Отметьте своё согласие!");
            return false;
        }
        return true;
    }
}
