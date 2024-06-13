package com.yogadimas.homecontroller;

import android.app.Application;

import com.firebase.client.Firebase;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Buka akses untuk koneksi ke firebase
        Firebase.setAndroidContext(this);


    }
}
