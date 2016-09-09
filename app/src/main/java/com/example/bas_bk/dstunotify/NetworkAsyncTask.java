package com.example.bas_bk.dstunotify;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;



/**
 * Created by BAS_BK on 29.08.2016.
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, String> {
    public static String SERVER_URL = "http://dstuns.ddns.net:3000/";
    private int flag;
    private Context context;
    public NetworkAsyncTask(Context context){
        this.context = context;
    }
    public NetworkAsyncTask(){

    }
    @Override
    protected String doInBackground(String...params) {
            String data = null;
            try {
                if (params.length == 2) {
                    data = (String) getClass().getDeclaredMethod(params[0], String.class).invoke(this, params[1]);
                } else if (params.length == 1) {
                    data = (String) getClass().getDeclaredMethod(params[0]).invoke(this);
                } else if (params.length == 3) {
                    data = (String) getClass().getDeclaredMethod(params[0], String.class, String.class).invoke(this, params[1], params[2]);
                } else if (params.length == 4) {
                    data = (String) getClass().getDeclaredMethod(params[0], String.class, String.class, String.class).invoke(this, params[1], params[2], params[3]);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.getCause();
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return data;
    }
    private String SignUp(String StudentJson){
        String data = null;
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(SERVER_URL + "Account/AddStudent?Student=" + StudentJson);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String SignIn(String Login, String Password){
        String data = null;
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(SERVER_URL + "Account/SignInStudent?Login=" + Login + "&Password=" + Password);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String GetFaculties(){
        flag = 1;
        String data = null;
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(SERVER_URL + "GetData/GetFaculties");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String GetDepartmentsFromFaculty(String FacultyName){
        flag = 2;
        String data = null;
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(SERVER_URL + "GetData/GetDepartmentsFromFaculty?FacultyName=" + FacultyName);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String GetGroupsFromDepartment(String DepartmentName){
        flag = 3;
        String data = null;
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(SERVER_URL + "GetData/GetGroupsFromDepartment?DepartmentName=" + DepartmentName);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String GetMessages(String Login, String Password, String New) throws JSONException {
        String data = null;
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(SERVER_URL + "Send/GetMessages?Login=" + Login + "&Password=" + Password + "&New=" + New);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000);
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String VerifyMessages(String IDs ,String Login, String Password) throws JSONException {
        String data = null;
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(SERVER_URL + "Send/VerifyMessages?mesId=" + IDs + "&Login=" + Login + "&Password=" + Password);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String MarkMessage(String id, String Login, String Password){
        String data = null;
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(SERVER_URL + "Send/MarkMessage?IdMessage=" + id + "&Login=" + Login + "&Password=" + Password);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(100);
            connection.connect();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(1000);
            data = ReadResponse(connection);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
            return "off";
        }catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "off";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "off";
        }
        return data;
    }

    private String ReadResponse(HttpURLConnection connection) {
        String data;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buf = new StringBuilder();
            while ((data=reader.readLine()) != null){
                buf.append(data);
            }
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "off";
        }
    }
    @Override
    protected void onPostExecute(String result){
        Gson gson = new Gson();
        if (flag == 1){
            SignUpActivity.facultySpin.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, gson.fromJson(result, String[].class)));
        }
        else if (flag == 2){
            SignUpActivity.depSpin.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, gson.fromJson(result, String[].class)));
        }
        else if (flag == 3){
            SignUpActivity.groupSpin.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, gson.fromJson(result, String[].class)));
        }
        super.onPostExecute(result);
    }

}
