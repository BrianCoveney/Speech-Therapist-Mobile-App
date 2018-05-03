package com.brian.speechtherapistapp.models;


import com.brian.speechtherapistapp.util.Const;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Word implements  Serializable{
    private static final long serialVersionUID = -8060210544600464481L;
    private String id;
    private String name;
    private int frequency;
    private List<String> glidingLiquidsWords = Const.CORRECT_WORDS_LIST;

    public Word() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public boolean hasMatch(String word, List<String> list) {
        for (String words : list) {
            if (words.equals(word)) {
                incrementFrequency();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

}
