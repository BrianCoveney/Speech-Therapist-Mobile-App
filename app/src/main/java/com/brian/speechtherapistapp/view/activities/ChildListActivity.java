package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.ChildAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChildListActivity extends BaseActivity {

    private ChildAdapter childAdapter;
    private List<Child> childList;

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.lv_child_list)
    ListView childListView;

    @Override
    protected int getContentView() {
        return R.layout.activity_childlist;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);

        // Resolves 'com.mongodb.MongoException: android.os.NetworkOnMainThreadException'
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        new PopulateListViewTask().execute();
    }


    private class PopulateListViewTask extends AsyncTask<Void, Void, List<Child>> {

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
        }
    }
}