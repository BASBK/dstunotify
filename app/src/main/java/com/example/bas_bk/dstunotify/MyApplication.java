package com.example.bas_bk.dstunotify;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by BAS_BK on 24.08.2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }
}
