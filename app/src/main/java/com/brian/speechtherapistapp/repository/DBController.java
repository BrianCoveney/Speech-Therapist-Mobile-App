package com.brian.speechtherapistapp.repository;

import com.brian.speechtherapistapp.models.Child;

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


    public Child getChildFromDB(int id) {
        return iChildRepository.getChild(id);
    }


}