package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brian.speechtherapistapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.btn_login_therapist)
    Button therapistLoginButton;

    @BindView(R.id.btn_login_child)
    Button childLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login_therapist, R.id.btn_login_child})
    public void onButtonClicked(View view) {
        boolean isLoginTherapist;
        boolean isLoginChild;

        switch (view.getId()) {
            case R.id.btn_login_therapist:
                isLoginTherapist = true;
                Intent therapistIntent = new Intent(this, LoginActivity.class);
                therapistIntent.putExtra("therapist_login", isLoginTherapist);
                startActivity(therapistIntent);
                break;
            case R.id.btn_login_child:
                isLoginChild = true;
                Intent childIntent = new Intent(this, LoginActivity.class);
                childIntent.putExtra("child_login", isLoginChild);
                startActivity(childIntent);
                break;
        }
    }
}
