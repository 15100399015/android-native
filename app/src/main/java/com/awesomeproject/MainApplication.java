package com.awesomeproject;

import android.app.Application;

import com.facebook.soloader.SoLoader;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, false);

    }

}
