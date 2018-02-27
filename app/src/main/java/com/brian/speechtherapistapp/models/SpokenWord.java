package com.brian.speechtherapistapp.models;


import com.brian.speechtherapistapp.util.Const;

import java.util.List;

public class SpokenWord {
    private String word;
    private int frequency;
    private List<String> glidingLiquidsWords = Const.GLIDING_OF_LIQUIDS_INVALID;

    public SpokenWord() { }

    public SpokenWord(String word) {
        this.word = word;
        this.frequency = 0;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public boolean hasMatch(String word) {
        for (String words : glidingLiquidsWords) {
            if (words.contains(word)) {
                incrementFrequency();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "SpokenWord{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }

}
