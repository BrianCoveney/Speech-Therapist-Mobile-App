package com.brian.speechtherapistapp.view;


public interface IChildView {
    int getChildId();
    String getFirstName();
    String getSecondName();
    String getEmail();
    String getDateOfBirth();
    String getPassword();
    void showChildSavedMessage();
    void displayFirstName(String firstName);
    void displaySecondName(String secondName);
    void displayEmail(String email);
}
