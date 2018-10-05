package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildResponse;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;
import java.util.Map;

import retrofit2.Callback;


public interface IChildPresenter {
    void saveChild();
    void setView(IChildView childView);
    void loadChildDetails();
    List<Child> getChildren();
    Child setWord(String currWord, String newWord, String email);
    void setGlidingWordsMap(Map<String, Integer> glidingLiquidsMap, String email);
    Child getChildWithEmail(String email);
    void deleteChildAccount(String email);

    // Retrofit
    void createUser();
    void deleteUser(String email);

    void getData(Callback<List<ChildResponse>> callback);
}
