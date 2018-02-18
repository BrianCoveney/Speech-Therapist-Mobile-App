package com.brian.speechtherapistapp.view.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.IChildView;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateChildActivity extends AppCompatActivity implements IChildView{

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child);
        ((MainApplication)getApplication()).getPresenterComponent().inject(this);

        ButterKnife.bind(this);

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
