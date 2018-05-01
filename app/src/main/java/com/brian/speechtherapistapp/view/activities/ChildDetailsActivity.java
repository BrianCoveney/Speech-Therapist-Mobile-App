package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;

public class ChildDetailsActivity extends BaseActivity{

    TextView firstNameTextView;
    TextView secondNameTextView;
    TextView emailTextView;
    TextView wordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstNameTextView = findViewById(R.id.tv_first_name_child_details);
        secondNameTextView = findViewById(R.id.tv_second_name_child_details);
        emailTextView = findViewById(R.id.tv_email_child_details);
        wordTextView = findViewById(R.id.tv_word_child_details);

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