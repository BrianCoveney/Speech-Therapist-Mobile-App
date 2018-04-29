package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildDetailsActivity extends BaseActivity{

    @BindView(R.id.tv_first_name)
    TextView firstNameTextView;

    @BindView(R.id.tv_second_name)
    TextView secondNameTextView;

    @BindView(R.id.tv_email)
    TextView emailTextView;

    @BindView(R.id.tv_word)
    TextView wordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Child child = getChildFromChildListActivity();

        setFields(child);
    }

    private void setFields(Child child) {
        firstNameTextView.setText(child.getFirstName());
        secondNameTextView.setText(child.getSecondName());
        emailTextView.setText(child.getEmail());
        wordTextView.setText(child.getWord());
    }

    private Child getChildFromChildListActivity() {
        Intent i = getIntent();
        Child c = i.getParcelableExtra("child_key");

        return Child.builder(c.getId(), c.getFirstName(), c.getSecondName(), c.getEmail())
                .withWord(c.getWord())
                .build();
    }

}
