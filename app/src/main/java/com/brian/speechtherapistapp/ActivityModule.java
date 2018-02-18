package com.brian.speechtherapistapp;


import com.brian.speechtherapistapp.view.LaunchActivityImpl;
import com.brian.speechtherapistapp.view.activities.ILaunchActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private MainApplication mainApplication;

    public ActivityModule(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Provides
    public ILaunchActivity providesLaunchActivity() {
        return new LaunchActivityImpl();
    }
}
