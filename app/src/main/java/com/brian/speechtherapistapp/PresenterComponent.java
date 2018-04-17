package com.brian.speechtherapistapp;


import com.brian.speechtherapistapp.view.activities.ChildListActivity;
import com.brian.speechtherapistapp.view.activities.CreateChildActivity;
import com.brian.speechtherapistapp.view.activities.GameOneActivity;
import com.brian.speechtherapistapp.view.activities.GameTwoActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { PresenterModule.class })
public interface PresenterComponent {
    void inject(CreateChildActivity createChildActivity);
    void inject(GameOneActivity gameOneActivity);
    void inject(GameTwoActivity gameTwoActivity);
    void inject(ChildListActivity childListActivity);
}
