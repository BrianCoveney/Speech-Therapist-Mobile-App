package com.brian.speechtherapistapp.controllers;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.repository.ChildRepositoryImpl;
import com.brian.speechtherapistapp.repository.IChildRepository;

import java.util.List;

public class DBController {

    private static DBController instance;
    private IChildRepository iChildRepository;

    // singleton
    public static DBController getInstance() {
        if (instance == null) {
            instance = new DBController();
        }
        return instance;
    }

    private DBController() {
        this.iChildRepository = new ChildRepositoryImpl();
    }


    public List<Child> selectChildListFromDB() {
        return iChildRepository.getChildListFromDB();
    }


}
