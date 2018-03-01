package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.view.IGameOneView;

public interface IWordPresenter {
    void setView(IGameOneView gameOneView);
    void saveWord();
    void loadWordDetails();
}
