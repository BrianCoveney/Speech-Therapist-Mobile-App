package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;

/**
 * Created by brian on 01/02/18.
 */

public interface IChildRepository {
    Child getChild(String id);
    void saveChild(ChildList childList);
}
