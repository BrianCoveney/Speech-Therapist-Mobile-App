package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.RetroChild;
import com.brian.speechtherapistapp.network.IWebAPIService;
import com.brian.speechtherapistapp.network.RetrofitClientInstance;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.btn_login_therapist)
    Button therapistLoginButton;
    @BindView(R.id.btn_login_child)
    Button childLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);

        ButterKnife.bind(this);

        connectAndGetApiData();
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

    private void connectAndGetApiData() {
        IWebAPIService service = RetrofitClientInstance.getRetrofitInstance()
                .create(IWebAPIService.class);

        Call<List<RetroChild>> call = service.getAllChildren();
        call.enqueue(new Callback<List<RetroChild>>() {
            @Override
            public void onResponse(Call<List<RetroChild>> call, Response<List<RetroChild>> response) {
                Log.i(LOG_TAG, "FOUND: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<List<RetroChild>> call, Throwable t) {
                Log.e(LOG_TAG,"ERROR: " +  t.toString());
            }
        });

    }
}
