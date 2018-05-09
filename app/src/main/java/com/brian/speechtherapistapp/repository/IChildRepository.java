package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;

import java.util.List;
import java.util.Map;

public interface IChildRepository {
    void saveChild(ChildList childList);

    Child updateWordSpoken(String currWord, String newWord, String email);
    void updateGlidingOfLiquidsMap(Map<String, Integer> glidingLiquidsMap, String email);
    List<Child> getChildListFromDB();
    Child getChildWithEmailIdentifier(String email);
    Child getChildFromDB();
}
