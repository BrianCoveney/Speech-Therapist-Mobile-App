package com.brian.speechtherapistapp.view;

import android.content.Context;
import android.content.Intent;

import com.brian.speechtherapistapp.view.activities.CreateChildActivity;
import com.brian.speechtherapistapp.view.activities.ILaunchActivity;
import com.brian.speechtherapistapp.view.activities.LoginActivity;
import com.brian.speechtherapistapp.view.activities.SplashActivity;

import javax.inject.Inject;

/**
 * Created by brian on 02/02/18.
 */

public class LaunchActivityImpl implements ILaunchActivity {

    @Inject
    public LaunchActivityImpl() { }

    public void launchMainActivity(Context context) {
        Intent mainIntent = new Intent(context, SplashActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainIntent);
    }

    public void launchLoginActivity(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }

    public void launchCreateChildActivity(Context context) {
        Intent createChildIntent = new Intent(context, CreateChildActivity.class);
        createChildIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(createChildIntent);
    }
}
