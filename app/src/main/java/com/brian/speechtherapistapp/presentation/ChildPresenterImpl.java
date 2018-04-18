package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.IChildRepository;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by brian on 04/02/18.
 */

public class ChildPresenterImpl implements IChildPresenter {

    private Child child;
    private ChildList childList;
    private IChildRepository childRepository;
    private IChildView childView;

    @Inject
    public ChildPresenterImpl() {
    }

    public ChildPresenterImpl(IChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public void setView(IChildView childView) {
        this.childView = childView;
        loadChildDetails();
    }

    @Override
    public void loadChildDetails() {
        int childId = childView.getChildId();
        child = childRepository.getChild(childId);

    }

    @Override
    public List<Child> getChildren() {
        return this.childRepository.getChildListFromDB();

    }

    @Override
    public void saveChild() {
        child.setFirstName(childView.getFirstName());
        child.setSecondName(childView.getSecondName());
        child.setEmail(childView.getEmail());
        child.setBirthday(childView.getDateOfBirth());
        child.setPassword(childView.getPassword());

        childList = new ChildList();
        childList.add(child);

        childRepository.saveChild(childList);

        childView.showChildSavedMessage();
    }
}
