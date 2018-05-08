package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;
import java.util.Map;

/**
 * Created by brian on 04/02/18.
 */

public interface IChildPresenter {
    void setView(IChildView childView);
    void saveChild();
    void loadChildDetails();
    List<Child> getChildren();
    Child setWord(String newWord, String email);

    void setGlidingWordsMap(Map<String, Integer> glidingLiquidsMap, String email);

    Child getChildWithEmail(String email);
}
