package com.brian.speechtherapistapp.view;

import android.content.Context;
import android.content.Intent;

/**
 * Created by brian on 02/02/18.
 */

public final class LaunchActivity {

    private LaunchActivity() {
        // Not called
    }

    public static void launchLoginActivity(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }
}
