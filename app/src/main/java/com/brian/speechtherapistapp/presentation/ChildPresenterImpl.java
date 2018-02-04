package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.repository.ChildRepository;

import javax.inject.Inject;

/**
 * Created by brian on 04/02/18.
 */

public class ChildPresenterImpl implements IChildPresenter {

    private Child child;
    private ChildRepository childRepository;

    @Inject
    public ChildPresenterImpl() {
    }

    public ChildPresenterImpl(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public void saveChild() {
        if (child != null) {

        }
    }
}
