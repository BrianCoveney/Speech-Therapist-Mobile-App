package com.brian.speechtherapistapp;

import android.app.Application;

public class MyApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                                      .appModule(new AppModule(this))
                                      .build();
    }

    public AppComponent getAppComponent() {
        return component;
    }

}
