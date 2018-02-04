package com.brian.speechtherapistapp.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.view.IChildView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements IChildView {

    @BindView(R.id.login_constraint_layout)
    ConstraintLayout constraintLayout;

    @Inject
    LaunchActivityImpl launchActivity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((MainApplication)getApplication()).getActivityComponent().inject(this);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel_button, R.id.submit_button})
    public void onButtonClicked(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                launchActivity.launchMainActivity(this);
                break;
            case R.id.submit_button:
                Snackbar.make(constraintLayout, "Submit clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public String getChildId() {
        return null;
    }

    @Override
    public void displayFirstName(String firstName) {

    }

    @Override
    public void displaySecondName() {

    }

    @Override
    public void showChildSavedMessage() {

    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public void showChildNameIsRequired() {

    }
}
