package com.brian.speechtherapistapp.view;


public interface IChildView {
    String getChildId();
    String getFirstName();
    String getSecondName();
    void showChildSavedMessage();
    void displayFirstName(String firstName);
    void displaySecondName(String secondName);
}
