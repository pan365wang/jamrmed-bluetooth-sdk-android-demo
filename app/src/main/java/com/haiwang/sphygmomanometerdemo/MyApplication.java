package com.haiwang.sphygmomanometerdemo;

import android.app.Application;

import com.haiwang.bluetooth.sphygmomanometer.event.MonitercenterManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MonitercenterManager.getInstance().init(this);
    }
}
