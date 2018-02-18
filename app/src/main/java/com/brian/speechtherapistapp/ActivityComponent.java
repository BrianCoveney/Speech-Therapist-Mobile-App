package com.brian.speechtherapistapp;

import com.brian.speechtherapistapp.view.activities.LoginActivity;
import com.brian.speechtherapistapp.view.activities.MainActivity;
import com.brian.speechtherapistapp.view.activities.TherapistMenuActivity;

import dagger.Component;


@Component(
        modules = { ActivityModule.class
})

public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(TherapistMenuActivity therapistMenuActivity);
}
