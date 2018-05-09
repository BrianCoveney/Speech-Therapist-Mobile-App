package com.brian.speechtherapistapp.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.ChildAdapter;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildListActivity extends BaseActivity {

    private ChildAdapter childAdapter;
    private List<Child> childList;
    private static final int DELAY_IN_MILLS = 500;

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

        new PopulateListViewTask().execute();

        onListItemClicked();
    }

    private class PopulateListViewTask extends AsyncTask<Void, Void, List<Child>> {

    private ProgressDialog progressDialog = new ProgressDialog(ChildListActivity.this);


    @Override
        protected List<Child> doInBackground(Void... voids) {
            // Call to DB
            childList = iChildPresenter.getChildren();
            return childList;
        }

        @Override
        protected void onPostExecute(List<Child> children) {
            super.onPostExecute(children);
            // Create and populate our custom adapter
            childAdapter = new ChildAdapter(getApplicationContext(), childList);
            childListView.setAdapter(childAdapter);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressDialog.dismiss();
                }
            }, DELAY_IN_MILLS);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Please wait");
            this.progressDialog.show();
        }
    }


    private void onListItemClicked() {
        childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Child childClicked = (Child) adapterView.getItemAtPosition(i);
                Child child = Child.builder(
                        childClicked.getId(),
                        childClicked.getFirstName(),
                        childClicked.getSecondName(),
                        childClicked.getEmail())
                        .withWord(childClicked.getWordName())
                        .build();

                Intent intent = new Intent(getApplicationContext(), ChildDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("child_key", child);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
