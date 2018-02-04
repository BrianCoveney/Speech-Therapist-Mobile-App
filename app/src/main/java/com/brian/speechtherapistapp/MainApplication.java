package com.brian.speechtherapistapp;

import android.app.Application;

import com.brian.speechtherapistapp.models.ActivityModule;

public class MainApplication extends Application {

    private ActivityComponent activityComponent;
    private PresenterComponent presenterComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        activityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .build();

        presenterComponent = DaggerPresenterComponent
                .builder()
                .presenterModule(new PresenterModule(this))
                .build();
    }


    public PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }

    public ActivityComponent getActivityComponent() { return activityComponent; }

}
