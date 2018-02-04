package com.brian.speechtherapistapp.view;


public interface IChildView {
    String getChildId();

    void displayFirstName(String firstName);
    void displaySecondName();

    void showChildSavedMessage();

    String getFirstName();
    String getLastName();

    void showChildNameIsRequired();
}
