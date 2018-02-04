package com.brian.speechtherapistapp.repository;


import com.brian.speechtherapistapp.models.Child;

import javax.inject.Inject;

public class DatabaseChildRepository implements ChildRepository {
    private Child child;

    @Inject
    public DatabaseChildRepository() {
    }

    @Override
    public Child getChild(String id) {
        if (child != null) {
            child = Child.builder(id, "Brian", "Coveney", "sdf").build();
        }
        return child;
    }

    @Override
    public void saveChild(Child c) {
        if (this.child == null) {
            this.child = getChild("0");
        }
        this.child = Child.builder(c.getId(), c.getFirstName(), c.getSecondName(), c.getEmail())
                          .build();
    }
}
