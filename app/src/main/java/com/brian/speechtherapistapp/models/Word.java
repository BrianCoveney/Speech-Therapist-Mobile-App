package com.brian.speechtherapistapp.models;


import com.brian.speechtherapistapp.util.Const;

import java.util.List;

public class Word {

    private String id;
    private String word;
    private int frequency;
    private List<String> glidingLiquidsWords = Const.GLIDING_OF_LIQUIDS_INVALID_LIST;

    public Word() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "Word{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }

}
