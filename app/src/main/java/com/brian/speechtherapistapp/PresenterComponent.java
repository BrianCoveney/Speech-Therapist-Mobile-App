package com.brian.speechtherapistapp;


import com.brian.speechtherapistapp.view.activities.CreateChildActivity;
import com.brian.speechtherapistapp.view.activities.GameOneActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { PresenterModule.class })
public interface PresenterComponent {
    void inject(CreateChildActivity createChildActivity);
    void inject(GameOneActivity gameOneActivity);
}
