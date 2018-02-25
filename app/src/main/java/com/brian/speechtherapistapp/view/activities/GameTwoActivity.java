package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brian.speechtherapistapp.R;

import butterknife.OnClick;

/**
 * Created by brian on 2/25/18.
 */

public class GameTwoActivity extends BaseActivity{

    @Override
    protected int getContentView() {
        return R.layout.activity_game_two;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
    }

    @OnClick(R.id.game_two_start_button)
    protected void onButtonClick() {
        showToast("Game Two Started!");
    }
}
