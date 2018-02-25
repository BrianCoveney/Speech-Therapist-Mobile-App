package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.IChildView;

import javax.inject.Inject;

import butterknife.BindView;


public class CreateChildActivity extends BaseActivity implements IChildView{

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.first_name_edit_text)
    EditText firstNameEditText;

    @BindView(R.id.second_name_edit_text)
    EditText secondNameEditText;

    @BindView(R.id.btn_save)
    Button saveButton;

    private static final String CHILD_ID = "child_id";

    @Override
    protected int getContentView() {
        return R.layout.activity_create_child;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        ((MainApplication)getApplication()).getPresenterComponent().inject(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iChildPresenter.saveChild();
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iChildPresenter.setView(this);
    }

    @Override
    public String getChildId() {
        return CHILD_ID;
    }

    @Override
    public String getFirstName() {
        return firstNameEditText.getText().toString();
    }

    @Override
    public String getSecondName() {
        return secondNameEditText.getText().toString();
    }

    @Override
    public void showChildSavedMessage() {
        Toast.makeText(this, R.string.child_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayFirstName(String firstName) {
        firstNameEditText.setText(firstName);
    }

    @Override
    public void displaySecondName(String secondName) {
        secondNameEditText.setText(secondName);
    }
}
