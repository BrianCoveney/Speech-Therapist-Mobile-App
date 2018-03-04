package com.brian.speechtherapistapp.view.activities;

import android.os.AsyncTask;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.Assets;


public class SetupSphinxRecognizerTask extends AsyncTask<Void, Void, Exception> {
    WeakReference<GameOneActivity> activityReference;
    SetupSphinxRecognizerTask(GameOneActivity activity) {
        this.activityReference = new WeakReference<>(activity);
    }
    @Override
    protected Exception doInBackground(Void... params) {
        try {
            Assets assets = new Assets(activityReference.get());
            File assetDir = assets.syncAssets();
            activityReference.get().setupRecognizer(assetDir);
        } catch (IOException e) {
            return e;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Exception result) {
        if (result != null) {
            activityReference.get().captionTextView.setText("Failed to init recognizer " + result);
        } else {
            activityReference.get().captionTextView.setVisibility(View.INVISIBLE);
        }
    }
}
