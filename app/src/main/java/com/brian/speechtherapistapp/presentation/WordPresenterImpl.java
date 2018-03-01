package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Word;
import com.brian.speechtherapistapp.repository.IWordRepository;
import com.brian.speechtherapistapp.view.IGameOneView;

import javax.inject.Inject;


public class WordPresenterImpl implements IWordPresenter {

    private IGameOneView gameOneView;
    private Word word;
    private IWordRepository wordRepository;

    @Inject
    public WordPresenterImpl(IWordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public void setView(IGameOneView gameOneView) {
        this.gameOneView = gameOneView;
        loadWordDetails();
    }

    @Override
    public void saveWord() {
        word.setWord(gameOneView.getRecognizerWordResult());
        wordRepository.saveWord(word);
    }

    @Override
    public void loadWordDetails() {
        String wordId = gameOneView.getWordId();
        word = wordRepository.getWord(wordId);
    }
}