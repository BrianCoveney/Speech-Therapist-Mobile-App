package com.brian.speechtherapistapp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brian.speechtherapistapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_floatingActionButton)
    FloatingActionButton loginFloatingActionButton;

    @Inject
    LaunchActivity launchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        openLoginScreen();
    }


    private void openLoginScreen() {
        loginFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchActivity.launchLoginActivity(getApplicationContext());
            }
        });
    }


}
