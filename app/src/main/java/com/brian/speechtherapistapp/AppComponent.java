package com.brian.speechtherapistapp;

import com.brian.speechtherapistapp.view.LoginActivity;
import com.brian.speechtherapistapp.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
}
