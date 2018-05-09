package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TherapistMenuActivity extends BaseActivity {

    @BindView(R.id.therapist_name_textview)
    TextView therapistNameTextView;

    @BindView(R.id.create_child_button)
    Button createChildButton;

    @BindView(R.id.view_child_list_button)
    Button viewChildListButton;

    private final String LOG_TAG = TherapistMenuActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_therapist_menu, frameLayout);
        ButterKnife.bind(this);
        getIntentFromCallingActivity();
    }

    private void getIntentFromCallingActivity() {
        Intent intent = getIntent();
        String messageReceived = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        SpannableStringBuilder nameBuilder = highlightWord(messageReceived);

        if (messageReceived != null) {
            therapistNameTextView.setText(nameBuilder, TextView.BufferType.SPANNABLE);
        }
    }

    private SpannableStringBuilder highlightWord(String word) {
        if (word != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString message = new SpannableString("Welcome ");
            builder.append(message);
            SpannableString name = new SpannableString(word);
            name.setSpan(new ForegroundColorSpan(Color.RED), 0, name.length(), 0);
            return builder.append(name);
        }
        return null;
    }

    @OnClick({R.id.create_child_button, R.id.view_child_list_button})
    public void onMenuButtonClicked(View view) {
        switch (view.getId()){
            case R.id.create_child_button:
                Intent intentStartCreateChildActivity = new Intent(this, CreateChildActivity.class);
                startActivity(intentStartCreateChildActivity);
                break;
            case R.id.view_child_list_button:
                Intent intentStartLoginActivity = new Intent(this, ChildListActivity.class);
                startActivity(intentStartLoginActivity);
                break;
        }
    }

}