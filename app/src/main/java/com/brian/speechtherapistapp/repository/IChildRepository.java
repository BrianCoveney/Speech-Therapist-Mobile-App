package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;

import java.util.List;
import java.util.Map;

public interface IChildRepository {
    void saveChild(ChildList childList);
    void updateGlidingOfLiquidsMap(Map<String, Integer> glidingLiquidsMap, String email);
    List<Child> getChildListFromDB();
    Child updateWordSpoken(String newWord, String email);
    Child getChildWithEmailIdentifier(String email);
    Child getChildFromDB();
}
