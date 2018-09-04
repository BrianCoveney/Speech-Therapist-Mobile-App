package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.PresenterModule;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.IChildRepository;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class ChildPresenterImpl implements IChildPresenter {
    private Child child;
    private IChildRepository iChildRepository;
    private IChildView iChildView;
    private static final String LOG_TAG = ChildPresenterImpl.class.getSimpleName();


    // Constructor Injection.
    // This required annotation by Dagger 2 to preform its code generation and provide dependencies
    @Inject
    public ChildPresenterImpl() { }

    /**
     * Constructor Injection for Dagger 2.
     * @see PresenterModule#providesChildPresenter(IChildRepository)
     */
    public ChildPresenterImpl(IChildRepository iChildRepository) {
        this.iChildRepository = iChildRepository;
    }

    @Override
    public void saveChild() {
        // Set our 'Child' Object attributes equal to the returned results from the View, i.e
        // the TextViews in CreateChildActivity.
        child.setFirstName(iChildView.getFirstName());
        child.setSecondName(iChildView.getSecondName());
        child.setEmail(iChildView.getEmail());
        child.setBirthday(iChildView.getDateOfBirth());
        child.setPassword(iChildView.getPassword());

        // Add this 'Child' object to our instantiated ChildList class
        ChildList childList = new ChildList();
        childList.add(child);

        // Calling the iChildRepository interface to save the childList
        iChildRepository.saveChild(childList);

        // Calling the iChildRepository to display a confirmation message in the CreateChildActivity
        iChildView.showChildSavedMessage();
    }

    @Override
    public List<Child> getChildren() {
        return this.iChildRepository.getChildListFromDB();
    }


    @Override
    public void setView(IChildView childView) {
        this.iChildView = childView;
        loadChildDetails();
    }

    @Override
    public void loadChildDetails() {
        child = iChildRepository.getChildFromDB();

        if (child != null) {
            iChildView.displayFirstName(child.getFirstName());
            iChildView.displaySecondName(child.getSecondName());
            iChildView.displayEmail(child.getEmail());
        }
    }

    @Override
    public Child setWord(String currWord, String newWord, String email) {
        return iChildRepository.updateWordSpoken(currWord, newWord, email);
    }

    @Override
    public void setGlidingWordsMap(Map<String, Integer> glidingLiquidsMap, String email) {
        iChildRepository.updateGlidingOfLiquidsMap(glidingLiquidsMap, email);
    }


    // Implementation of the interface IChildPresenter Child getChildWithEmail(String email) method
    @Override
    public Child getChildWithEmail(String email) {
        return iChildRepository.getChildWithEmailIdentifier(email);
    }

    @Override
    public void deleteChildAccount(String email) {
        iChildRepository.deleteChild(email);
    }
}
