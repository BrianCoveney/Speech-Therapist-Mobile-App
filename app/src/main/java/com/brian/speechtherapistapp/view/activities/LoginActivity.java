package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.view.LaunchActivityImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @Inject
    LaunchActivityImpl launchActivity;

    @BindView(R.id.login_constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView((R.id.username_edit_text))
    EditText userNameEditText;

    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    public static final String EXTRA_MESSAGE = "therapist_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ((MainApplication)getApplication()).getActivityComponent().inject(this);
    }

    @OnClick({R.id.login_button, R.id.skip_button})
    public void onButtonClicked(View view) {
        switch (view.getId()){
            case R.id.login_button:
                onTherapistSignIn();
                break;
            case R.id.skip_button:
                Intent intent = new Intent(this, SpeechActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void onTherapistSignIn() {
        Intent intent = new Intent(this, TherapistMenuActivity.class);
        String therapistName = userNameEditText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, therapistName);
        startActivity(intent);
    }

}
