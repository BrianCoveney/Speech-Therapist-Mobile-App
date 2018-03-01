package com.brian.speechtherapistapp;

import android.app.Application;

public class MainApplication extends Application {

    private PresenterComponent presenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        presenterComponent = DaggerPresenterComponent.builder()
                .presenterModule(new PresenterModule(this))
                .build();
    }

    public PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }
}
