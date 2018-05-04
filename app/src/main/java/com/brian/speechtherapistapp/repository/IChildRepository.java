package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;

import java.util.List;

public interface IChildRepository {
    void saveChild(ChildList childList);
    List<Child> getChildListFromDB();
    Child updateWordSpoken(String currWord, String newWord, String email);
    Child getChildWithEmailIdentifier(String email);
    Child getChildFromDB();
}
