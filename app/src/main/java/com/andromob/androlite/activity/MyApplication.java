package com.andromob.androlite.activity;

import android.app.Application;

import com.andromob.androlite.utils.Config;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    public MyApplication(){
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSignal.initWithContext(this);
        OneSignal.setAppId(Config.ONESIGNAL_APP_ID);
    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }
}