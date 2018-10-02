package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.RetroChild;
import com.brian.speechtherapistapp.network.IWebAPIService;
import com.brian.speechtherapistapp.network.RetrofitClientInstance;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.ChildAdapter;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildListActivity extends BaseActivity {

    private ChildAdapter childAdapter;
    private static final String LOG_TAG = ChildListActivity.class.getSimpleName();

    @Inject
    IChildPresenter iChildPresenter;
    @BindView(R.id.lv_child_list)
    ListView childListView;
    @BindView(R.id.progress_bar_cyclic)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);
        getLayoutInflater().inflate(R.layout.activity_childlist, frameLayout);
        ButterKnife.bind(this);

        // Resolves 'com.mongodb.MongoException: android.os.NetworkOnMainThreadException'
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        connectAndGetApiData();

        onListItemClicked();
    }

    private void onListItemClicked() {
        childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RetroChild childClicked = (RetroChild) adapterView.getItemAtPosition(i);
                String firstName = childClicked.getFirstName();
                String secondName = childClicked.getSecondName();
                String email = childClicked.getEmail();
                String word = childClicked.getWord();

                Child child = Child.builder(firstName, secondName, email)
                        .withWord(word)
                        .build();

                Intent intent = new Intent(getApplicationContext(), ChildDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("child_key", child);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void connectAndGetApiData() {
        IWebAPIService service = RetrofitClientInstance.getRetrofitInstance()
                .create(IWebAPIService.class);

        Call<List<RetroChild>> call = service.getAllChildren();
        call.enqueue(new Callback<List<RetroChild>>() {
            @Override
            public void onResponse(Call<List<RetroChild>> call, Response<List<RetroChild>> response) {
                Log.i(LOG_TAG, "FOUND: " + response.body().toString());
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<RetroChild>> call, Throwable t) {
                Log.e(LOG_TAG,"ERROR: " +  t.toString());
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<RetroChild> children) {
        childAdapter = new ChildAdapter(this, children);
        childListView.setAdapter(childAdapter);
    }
}
