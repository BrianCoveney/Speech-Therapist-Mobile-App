package com.brian.speechtherapistapp.models;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Word implements  Serializable{

    private static final long serialVersionUID = -8060210544600464481L;
    private String id;
    private String name;

    private int frequency;
    private Map<String, Integer> glidingLiquidsMap = new HashMap<>();

    public Word() { }

    public Word(String name) {
        this.name = name;
    }

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

    public Map<String, Integer> getGlidingLiquidsMap() {
        return glidingLiquidsMap;
    }

    public void setGlidingLiquidsMap(Map<String, Integer> glidingLiquidsMap) {
        this.glidingLiquidsMap = glidingLiquidsMap;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public boolean hasMatch(String wordSpoken, List<String> wordList) {
        for (String words : wordList) {
            if (words.equals(wordSpoken)) {
                incrementFrequency();
                return true;
            }
        }
        return false;
    }

    public Map<String, Integer> updateMap(String wordSpoken) {
        if (glidingLiquidsMap.containsKey(wordSpoken)) {
            glidingLiquidsMap.put(wordSpoken, glidingLiquidsMap.get(wordSpoken) + 1);
        } else {
            glidingLiquidsMap.put(wordSpoken, 1);
        }
        return glidingLiquidsMap;
    }


    @Override
    public String toString() {
        return name;
    }


}
