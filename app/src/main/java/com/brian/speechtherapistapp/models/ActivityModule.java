package com.brian.speechtherapistapp.models;


import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.view.activities.ILaunchActivity;
import com.brian.speechtherapistapp.view.activities.LaunchActivityImpl;

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
