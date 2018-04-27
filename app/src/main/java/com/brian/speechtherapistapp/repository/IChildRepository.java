package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;

import java.util.List;

/**
 * Created by brian on 01/02/18.
 */

public interface IChildRepository {
    Child getChild(int id);
    void saveChild(ChildList childList);
    List<Child> getChildListFromDB();
    void updateWordSpoken(Child child, String currWord, String newWord);

}
