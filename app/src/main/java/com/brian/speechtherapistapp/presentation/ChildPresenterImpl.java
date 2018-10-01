package com.brian.speechtherapistapp.presentation;

import android.util.Log;

import com.brian.speechtherapistapp.PresenterModule;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.models.RetroChild;
import com.brian.speechtherapistapp.network.IWebAPIService;
import com.brian.speechtherapistapp.network.RetrofitClientInstance;
import com.brian.speechtherapistapp.repository.IChildRepository;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        /* Instead of calling the MongoDB driver, we will connect to our REST Api to save the user*/
        //iChildRepository.saveChild(childList);

        //iChildView.showChildSavedMessage();

        connectAndPostApiData();
    }

    // Using Retrofit to Connect to our REST Api and Post a new user
    private void connectAndPostApiData() {
        IWebAPIService service = RetrofitClientInstance.getRetrofitInstance()
                .create(IWebAPIService.class);

        String firstName = iChildView.getFirstName();
        String secondName = iChildView.getSecondName();
        String email = iChildView.getEmail();

        RetroChild retroChild = new RetroChild(firstName, secondName, email);

        Call<RetroChild> call = service.createChildWithField(retroChild.getFirstName(),
                                                             retroChild.getSecondName(),
                                                             retroChild.getEmail());
        call.enqueue(new Callback<RetroChild>() {
            @Override
            public void onResponse(Call<RetroChild> call, Response<RetroChild> response) {
                RetroChild rChild = response.body();
                Log.i(LOG_TAG, "CREATED: " + rChild.getFirstName() + " "
                        + rChild.getSecondName() + " " + rChild.getEmail() + " "
                        + rChild.getWord() + " " + rChild.getGlidingLiquidsMap() + " ");
                iChildView.showChildSavedMessage();
            }

            @Override
            public void onFailure(Call<RetroChild> call, Throwable t) {

            }
        });
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
