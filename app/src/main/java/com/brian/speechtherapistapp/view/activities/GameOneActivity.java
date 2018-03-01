package com.brian.speechtherapistapp.view.activities;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IWordPresenter;
import com.brian.speechtherapistapp.util.Const;
import com.brian.speechtherapistapp.view.IGameOneView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class GameOneActivity extends BaseActivity implements IGameOneView, RecognitionListener {

    @Inject
    IWordPresenter wordPresenter;

    private static final String WORD_ID = "word_id";

    private final String LOG_TAG = GameOneActivity.class.getSimpleName();

    private ArrayAdapter<String> arrayAdapter;
    private List words = Const.GLIDING_OF_LIQUIDS_VALID;

    SharedPreferences sharedPreferences;
    public static final String KEY = "key";
    public static final String SPEECH_PREFERENCES = "Speech_Shared_Preferences";

    @BindView(R.id.game_one_list_view)
    ListView listView;

    @BindView(R.id.caption_text_2)
    TextView captionTextView;

    @BindView(R.id.result_text_2)
    TextView resultTextView;

    @BindView(R.id.start_recording_button)
    Button startRecordingButton;

    @BindView(R.id.stop_recording_button)
    Button stopRecordingButton;

    @BindView(R.id.test_edit_text)
    TextView editText;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "words";

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    private static final String TEXT_SEARCH = "words";


    @Override
    protected int getContentView() {
        return R.layout.activity_game_one;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        ((MainApplication)getApplication()).getPresenterComponent().inject(this);

        populateListView();

        stopRecordingButton.setVisibility(View.INVISIBLE);

        // Prepare the data for UI
        captions = new HashMap<>();
        captions.put(TEXT_SEARCH, R.string.test_caption);

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wordPresenter.setView(this);
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<GameOneActivity> activityReference;
        SetupTask(GameOneActivity activity) {
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

    private void populateListView() {
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, words);
        listView.setAdapter(arrayAdapter);
    }

    /*
      ----------------------------------------------------------------------------------------------
      CMU Speech Recognition Listeners
      ----------------------------------------------------------------------------------------------
    */
    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .getRecognizer();
        recognizer.addListener(this);

        // Create grammar-based search for custom recognition
        File testGrammar = new File(assetsDir, "words-gliding-of-liquids.gram");
        recognizer.addGrammarSearch(TEXT_SEARCH, testGrammar);
    }

    public void onStartButtonClick(View view) {
        recognizer.startListening(TEXT_SEARCH);
        captionTextView.setVisibility(View.VISIBLE);
        captionTextView.setText(startRecordingButton.getText());
        stopRecordingButton.setVisibility(View.VISIBLE);
        startRecordingButton.setVisibility(View.INVISIBLE);

        wordPresenter.saveWord();
    }

    public void onStopButtonClick(View view) {
        stopRecordingButton.setVisibility(View.INVISIBLE);
        startRecordingButton.setVisibility(View.VISIBLE);
        recognizer.stop();
        recognizer.cancel();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        resultTextView.setText(text);
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            showToast(text);
            Log.i(LOG_TAG, "onResult: " + text);
        }
    }

    @Override
    public void onError(Exception error) {
        captionTextView.setText(error.getMessage());
    }

    @Override
    public void onTimeout() { }

    @Override
    public void onBeginningOfSpeech() { }

    @Override
    public void onEndOfSpeech() { }

    /*
      ----------------------------------------------------------------------------------------------
      MVP
      ----------------------------------------------------------------------------------------------
    */
    @Override
    public String getWordId() {
        return WORD_ID;
    }

    @Override
    public String getRecognizerWordResult() {
        return startRecordingButton.getText().toString();
    }

}
