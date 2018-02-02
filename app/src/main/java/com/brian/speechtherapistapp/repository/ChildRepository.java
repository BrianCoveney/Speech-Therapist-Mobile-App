package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;

/**
 * Created by brian on 01/02/18.
 */

public interface ChildRepository {
    Child getChild(int id);
    void saveChild(Child child);
}
