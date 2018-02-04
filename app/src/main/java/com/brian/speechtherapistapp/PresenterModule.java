package com.brian.speechtherapistapp;

import com.brian.speechtherapistapp.presentation.ChildPresenterImpl;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.repository.ChildRepository;
import com.brian.speechtherapistapp.repository.DatabaseChildRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class PresenterModule {

    private MainApplication mainApplication;

    public PresenterModule(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Provides @Singleton
    public ChildRepository providesChildRepository() {
        return new DatabaseChildRepository();
    }

    @Provides
    public IChildPresenter providesChildPresenter(ChildRepository childRepository) {
        return new ChildPresenterImpl();
    }
}
