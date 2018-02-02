package com.brian.speechtherapistapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.brian.speechtherapistapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brian on 02/02/18.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_constraint_layout)
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel_button, R.id.submit_button})
    public void onButtonClicked(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                Snackbar.make(constraintLayout, "Cancel clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submit_button:
                Snackbar.make(constraintLayout, "Submit clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
