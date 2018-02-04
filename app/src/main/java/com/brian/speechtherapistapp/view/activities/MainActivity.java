package com.brian.speechtherapistapp.view.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_fab)
    FloatingActionButton loginFloatingActionButton;

    @Inject
    LaunchActivityImpl launchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ((MainApplication)getApplication()).getAppComponent().inject(this);

        ((MainApplication)getApplication()).getActivityComponent().inject(this);

        ButterKnife.bind(this);

        openLoginScreen();
    }


    private void openLoginScreen() {
        loginFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity.launchLoginActivity(getApplicationContext());
            }
        });
    }

}
