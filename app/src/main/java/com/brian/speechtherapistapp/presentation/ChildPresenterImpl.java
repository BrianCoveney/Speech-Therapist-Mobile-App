package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.IChildRepository;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;

import javax.inject.Inject;


public class ChildPresenterImpl implements IChildPresenter {

    private Child child;
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
        if (child != null) {
            childView.displayFirstName(child.getFirstName());
            childView.displaySecondName(child.getSecondName());
            childView.displayEmail(child.getEmail());
        }
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

        ChildList childList = new ChildList();
        childList.add(child);

        childRepository.saveChild(childList);

        childView.showChildSavedMessage();
    }

    @Override
    public Child setWord(Child child, String currWord, String newWord) {
        return childRepository.updateWordSpoken(child, currWord, newWord);
    }

    public Child getChildFromDB(int id) {
        return childRepository.getChild(id);
    }
}
