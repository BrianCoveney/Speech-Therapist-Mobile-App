package com.brian.speechtherapistapp.view.activities;


import android.content.Intent;
import android.os.Bundle;

import com.brian.speechtherapistapp.R;

import butterknife.OnClick;

public class GameOneActivity extends BaseActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_game_one;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
    }

    @OnClick(R.id.game_one_start_button)
    protected void onClickButton() {
        showToast("Game Started!");
    }

}
