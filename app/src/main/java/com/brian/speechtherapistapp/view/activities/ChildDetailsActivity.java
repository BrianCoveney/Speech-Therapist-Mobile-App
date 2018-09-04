package com.brian.speechtherapistapp.view.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import receivers.EmailDialog;

public class ChildDetailsActivity extends BaseActivity {

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.tv_first_name)
    TextView firstNameTextView;

    @BindView(R.id.tv_second_name)
    TextView secondNameTextView;

    @BindView(R.id.tv_email)
    TextView emailTextView;

    @BindView(R.id.tv_word)
    TextView wordTextView;

    @BindView(R.id.tv_gliding_map)
    TextView glidingTextView;


    @BindView(R.id.tv_gliding_caption)
    TextView glidingCaptionTextView;


    private Child child;
    private static final String LOG_TAG = ChildDetailsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);

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
        wordTextView.setText(child.getWordName());

        String glidingWords = "";
        Map<String, Integer> glidingMap = child.getWordGlidingLiquidsMap();

        for (String key : glidingMap.keySet()) {
            glidingWords += key + " -> " + glidingMap.get(key) + "\n";
        }

        glidingTextView.setText(glidingWords);

        glidingCaptionTextView.setText("Gliding of Liquids \nWord and Frequency:");

    }

    private Child getChildFromChildListActivity() {
        Intent i = getIntent();
        child = i.getParcelableExtra("child_key");

        // I had issues with getting HashMap of Words with Parcelable, so instead I'm querying the DB
        Child c = iChildPresenter.getChildWithEmail(child.getEmail());
        Map<String, Integer> wordGlidingMap = c.getWordGlidingLiquidsMap();

        return Child.builder(child.getId(), child.getFirstName(), child.getSecondName(), child.getEmail())
                .withWord(child.getWordName())
                .withGlidingWordMap(wordGlidingMap)
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

    @OnClick(R.id.fab_delete)
    public void onClickDelete() {
        Intent i = getIntent();
        Child child = i.getParcelableExtra("child_key");
        if (child != null) {
            iChildPresenter.deleteChildAccount(child.getEmail());
            showToast("Deleted username: " + child.getEmail());
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent = new Intent(ChildDetailsActivity.this, ChildListActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}
