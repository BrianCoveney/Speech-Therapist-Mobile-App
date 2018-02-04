package com.brian.speechtherapistapp.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IChildPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brian on 04/02/18.
 */

public class CreateChildActivity extends AppCompatActivity {

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.first_name_edit_text)
    EditText firstNameEditText;

    @BindView(R.id.second_name_edit_text)
    EditText secondNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child);

        ((MainApplication)getApplication()).getPresenterComponent().inject(this);

        ButterKnife.bind(this);

    }
}
