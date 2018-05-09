package com.brian.speechtherapistapp;


import com.brian.speechtherapistapp.view.activities.ChildDetailsActivity;
import com.brian.speechtherapistapp.view.activities.ChildListActivity;
import com.brian.speechtherapistapp.view.activities.CreateChildActivity;
import com.brian.speechtherapistapp.view.activities.GameActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { PresenterModule.class })
public interface PresenterComponent {
    void inject(CreateChildActivity createChildActivity);
    void inject(GameActivity gameOneActivity);
    void inject(ChildListActivity childListActivity);
    void inject(ChildDetailsActivity childDetailsActivity);
}
