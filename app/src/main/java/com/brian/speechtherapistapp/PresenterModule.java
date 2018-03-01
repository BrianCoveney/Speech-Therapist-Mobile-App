package com.brian.speechtherapistapp;

import com.brian.speechtherapistapp.presentation.ChildPresenterImpl;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.presentation.IWordPresenter;
import com.brian.speechtherapistapp.presentation.WordPresenterImpl;
import com.brian.speechtherapistapp.repository.ChildRepositoryImpl;
import com.brian.speechtherapistapp.repository.IChildRepository;
import com.brian.speechtherapistapp.repository.IWordRepository;
import com.brian.speechtherapistapp.repository.WordRepositoryImpl;

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
    public IChildRepository providesChildRepository() {
        return new ChildRepositoryImpl();
    }

    @Provides
    public IChildPresenter providesChildPresenter(IChildRepository childRepository) {
        return new ChildPresenterImpl(childRepository);
    }

    @Provides @Singleton
    public IWordRepository providesWordRepository() { return new WordRepositoryImpl(); }

    @Provides
    public IWordPresenter providesWordPresenter(IWordRepository wordRepository) {
        return new WordPresenterImpl(wordRepository);
    }

}
