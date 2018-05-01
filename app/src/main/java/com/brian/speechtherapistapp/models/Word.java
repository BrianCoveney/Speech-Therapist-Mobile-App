package com.brian.speechtherapistapp.models;


import com.brian.speechtherapistapp.util.Const;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Word implements Serializable{
    private static final long serialVersionUID = -8060210544600464481L;
    private String id;
    private String word;
    private int frequency;
    private List<String> glidingLiquidsWords = Const.LIST_OF_CORRECT_WORDS;

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
        return word;
    }

}
