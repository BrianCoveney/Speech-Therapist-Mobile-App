package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;

import butterknife.BindView;

public class ChildDetailsActivity extends BaseActivity{

    @BindView(R.id.box1)
    TextView textView;

    @Override
    protected int getContentView() {
        return R.layout.activity_child_details;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        Intent i = getIntent();
        Child child = i.getParcelableExtra("child_key");

        showToast("Here: "+ child);

    }
}
