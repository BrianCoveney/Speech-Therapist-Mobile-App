package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Word;


public interface IWordRepository {
    Word getWord(String word);
    void saveWord(Word word);
}
