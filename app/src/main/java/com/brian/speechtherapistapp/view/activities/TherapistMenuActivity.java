package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class TherapistMenuActivity extends AppCompatActivity{

    @BindView(R.id.therapist_name_textview)
    TextView therapistNameTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_menu);

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
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString message = new SpannableString("Welcome ");
        builder.append(message);
        SpannableString name = new SpannableString(word);
        name.setSpan(new ForegroundColorSpan(Color.RED), 0, name.length(), 0);
        return builder.append(name);
    }
}
