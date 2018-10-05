package com.brian.speechtherapistapp.presentation;

import android.util.Log;

import com.brian.speechtherapistapp.PresenterModule;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.models.ChildResponse;
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
    private List<ChildResponse> childResponses;
    private ChildPresenterImpl childPresenter;

    IWebAPIService apiService;
    Callback<List<ChildResponse>> responseCallback;

    // Constructor Injection.
    // This required annotation by Dagger 2 to preform its code generation and provide dependencies
    @Inject
    public ChildPresenterImpl() {
    }

    /**
     * Constructor Injection for Dagger 2.
     * @see PresenterModule#providesChildPresenter(IChildRepository)
     */
    public ChildPresenterImpl(IChildRepository iChildRepository) {
        this.iChildRepository = iChildRepository;

        childPresenter = new ChildPresenterImpl();

        apiService = RetrofitClientInstance.getRetrofitInstance()
                .create(IWebAPIService.class);
    }


    /**
     * Instead of calling the MongoDB driver using this saveChild() method, we are using  the
     * createUser() method below to consume our REST API.
     */
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

        iChildRepository.saveChild(childList);

        iChildView.showChildSavedMessage();
    }

    // Using Retrofit to Connect to our REST Api and Post a new user
    @Override
    public void createUser() {
        String firstName = iChildView.getFirstName();
        String secondName = iChildView.getSecondName();
        String email = iChildView.getEmail();

        ChildResponse childResponse = new ChildResponse(firstName, secondName, email);

        Call<ChildResponse> call = apiService.createChildWithField(childResponse.getFirstName(),
                                                             childResponse.getSecondName(),
                                                             childResponse.getEmail());
        call.enqueue(new Callback<ChildResponse>() {
            @Override
            public void onResponse(Call<ChildResponse> call, Response<ChildResponse> response) {
                ChildResponse rChild = response.body();
                Log.i(LOG_TAG, "CREATED: " + rChild.getFirstName() + " "
                        + rChild.getSecondName() + " " + rChild.getEmail());
                iChildView.showChildSavedMessage();
            }

            @Override
            public void onFailure(Call<ChildResponse> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
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


    /**
     * Instead of calling the MongoDB driver using this deleteChildAccount() method, we are using  the
     * deleteUser() method below to consume our REST API.
     */
    @Override
    public void deleteChildAccount(String email) {
        iChildRepository.deleteChild(email);
    }

    @Override
    public void deleteUser(String email) {
        Call<ChildResponse> call = apiService.deleteChildWithField(email);
        call.enqueue(new Callback<ChildResponse>() {
            @Override
            public void onResponse(Call<ChildResponse> call, Response<ChildResponse> response) {
                ChildResponse rChild = response.body();
                Log.i(LOG_TAG, "Deleted user: " + rChild.getFirstName());
            }

            @Override
            public void onFailure(Call<ChildResponse> call, Throwable t) {
                Log.e("DELETE ERROR: ", t.getMessage());
            }
        });
    }

    @Override
    public void getData(Callback<List<ChildResponse>> callback) {
        apiService.getAllChildren().enqueue(callback);
    }

}
