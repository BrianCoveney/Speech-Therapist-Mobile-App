package com.brian.speechtherapistapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class RetroChild {

    @SerializedName("_id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("second_name")
    private String secondName;

    @SerializedName("email")
    private String email;

    @SerializedName("word")
    private String word;

    @SerializedName("map_of_gliding_words")
    private Map<String, Integer> glidingLiquidsMap = new HashMap<>();

    public RetroChild(String firstName, String secondName, String email) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
    }

    public RetroChild(String firstName, String secondName, String email, String word,
                      Map<String, Integer> glidingLiquidsMap) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.word = word;
        this.glidingLiquidsMap = glidingLiquidsMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<String, Integer> getGlidingLiquidsMap() {
        return glidingLiquidsMap;
    }

    public void setGlidingLiquidsMap(Map<String, Integer> glidingLiquidsMap) {
        this.glidingLiquidsMap = glidingLiquidsMap;
    }
}