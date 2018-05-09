package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.IChildRepository;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class ChildPresenterImpl implements IChildPresenter {

    private static final String LOG_TAG = ChildPresenterImpl.class.getSimpleName();
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
        child = childRepository.getChildFromDB();

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
    public Child setWord(String currWord, String newWord, String email) {
        return childRepository.updateWordSpoken(currWord, newWord, email);
    }

    @Override
    public void setGlidingWordsMap(Map<String, Integer> glidingLiquidsMap, String email) {
        childRepository.updateGlidingOfLiquidsMap(glidingLiquidsMap, email);
    }


    @Override
    public Child getChildWithEmail(String email) {
        return childRepository.getChildWithEmailIdentifier(email);
    }

}
