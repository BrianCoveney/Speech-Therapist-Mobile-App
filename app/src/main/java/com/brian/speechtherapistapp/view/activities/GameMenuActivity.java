package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brian.speechtherapistapp.R;

import butterknife.OnClick;


public class GameMenuActivity extends BaseActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_game_menu;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
    }


    @OnClick({R.id.game_one_button, R.id.game_two_button, R.id.game_three_button})
    public void onButtonClicked(View view) {

        switch (view.getId()) {
            case R.id.game_one_button:
                Intent intentGameOneActivity = new Intent(this, GameOneActivity.class);
                startActivity(intentGameOneActivity);
                break;
            case R.id.game_two_button:
                Intent intentGameTwoActivity = new Intent(this, GameTwoActivity.class);
                startActivity(intentGameTwoActivity);
                break;
            case R.id.game_three_button:
                Intent intentGameThree = new Intent(this, SpeechActivity.class);
                startActivity(intentGameThree);;
                break;
        }

    }
}
