package com.brian.speechtherapistapp;

import com.brian.speechtherapistapp.models.ActivityModule;
import com.brian.speechtherapistapp.view.activities.LoginActivity;
import com.brian.speechtherapistapp.view.activities.MainActivity;

import dagger.Component;


@Component(
        modules = { ActivityModule.class
})

public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
}
