package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.view.IGameView;

public interface IWordPresenter {
    void setView(IGameView gameOneView);
    void saveWord();
    void loadWordDetails();
}
