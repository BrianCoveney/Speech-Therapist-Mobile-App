package com.brian.speechtherapistapp;

import com.brian.speechtherapistapp.view.ILaunchActivity;
import com.brian.speechtherapistapp.view.LaunchActivityImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MyApplication myApplication;

    public AppModule(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    public ILaunchActivity providesLaunchActivity() {
        return new LaunchActivityImpl();
    }
}
