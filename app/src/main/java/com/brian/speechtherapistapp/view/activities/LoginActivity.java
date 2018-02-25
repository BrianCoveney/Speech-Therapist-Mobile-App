package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;

import com.brian.speechtherapistapp.R;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView((R.id.username_edit_text))
    EditText userNameEditText;

    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    public static final String EXTRA_MESSAGE = "therapist_name";


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
    }

    @OnClick({R.id.login_button, R.id.skip_button})
    public void onButtonClicked(View view) {
        switch (view.getId()){
            case R.id.login_button:
                Intent intentTherapistMenuActivity = new Intent(this, TherapistMenuActivity.class);
                String therapistName = userNameEditText.getText().toString();
                intentTherapistMenuActivity.putExtra(EXTRA_MESSAGE, therapistName);
                startActivity(intentTherapistMenuActivity);
                break;
            case R.id.skip_button:
                Intent intentSpeechActivity = new Intent(this, SpeechActivity.class);
                startActivity(intentSpeechActivity);
                break;
        }
    }
}
