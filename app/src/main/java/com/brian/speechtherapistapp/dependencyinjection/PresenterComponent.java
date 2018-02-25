package com.brian.speechtherapistapp.dependencyinjection;


import com.brian.speechtherapistapp.view.activities.CreateChildActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { PresenterModule.class })
public interface PresenterComponent {
    void inject(CreateChildActivity createChildActivity);
}
