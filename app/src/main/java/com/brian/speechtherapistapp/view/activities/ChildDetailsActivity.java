package com.brian.speechtherapistapp.view.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import receivers.EmailDialog;

public class ChildDetailsActivity extends BaseActivity{

    @BindView(R.id.tv_first_name)
    TextView firstNameTextView;

    @BindView(R.id.tv_second_name)
    TextView secondNameTextView;

    @BindView(R.id.tv_email)
    TextView emailTextView;

    @BindView(R.id.tv_word)
    TextView wordTextView;

    private Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is where we get our content view
        getLayoutInflater().inflate(R.layout.activity_child_details, frameLayout);

        ButterKnife.bind(this);

        Child child = getChildFromChildListActivity();
        setFields(child);
    }

    private void setFields(Child child) {
        firstNameTextView.setText(child.getFirstName());
        secondNameTextView.setText(child.getSecondName());
        emailTextView.setText(child.getEmail());
        wordTextView.setText(child.getWordSaid());
    }

    private Child getChildFromChildListActivity() {
        Intent i = getIntent();
        child = i.getParcelableExtra("child_key");

        return Child.builder(child.getId(), child.getFirstName(), child.getSecondName(), child.getEmail())
                .withWord(child.getWordSaid())
                .build();
    }

    @OnClick(R.id.fab_email)
    public void onClickEmailButton() {
        DialogFragment dialogFragment = new EmailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("child_object", child);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "email_key");
    }

}
