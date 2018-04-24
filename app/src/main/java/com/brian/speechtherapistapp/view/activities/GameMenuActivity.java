package com.brian.speechtherapistapp.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.brian.speechtherapistapp.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.OnClick;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class GameMenuActivity extends BaseActivity implements RecognitionListener {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer recognizer;

    @Override
    protected int getContentView() {
        return R.layout.activity_game_menu;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();;
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
                startActivity(intentGameThree);
                break;
        }
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<GameMenuActivity> activityReference;
        SetupTask(GameMenuActivity activity) {
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
                String toastMessage = "Failed to init recognizer " + result;
                Toast.makeText(activityReference.get(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* We look for permission access to the mic, so as not to request it in the Games */
    protected void setupRecognizer(File assetsDir) throws IOException {
        try {
            recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                    .setRawLogDir(assetsDir)
                    .getRecognizer();
            recognizer.addListener(this);

            // Create grammar-based search for custom recognition
            File testGrammar = new File(assetsDir, "words-gliding-of-liquids.gram");
            recognizer.addGrammarSearch("words", testGrammar);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // None of these are called
    @Override
    public void onBeginningOfSpeech() { }

    @Override
    public void onEndOfSpeech() { }

    @Override
    public void onPartialResult(Hypothesis hypothesis) { }

    @Override
    public void onResult(Hypothesis hypothesis) { }

    @Override
    public void onError(Exception e) { }

    @Override
    public void onTimeout() { }
}
