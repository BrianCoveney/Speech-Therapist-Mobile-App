package com.brian.speechtherapistapp.view.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.brian.speechtherapistapp.view.IGameView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class GameOneActivity extends BaseActivity implements IGameView, RecognitionListener {

    @Inject
    IWordPresenter wordPresenter;

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


    private static final String LOG_TAG = GameOneActivity.class.getSimpleName();
    private static final String WORD_ID = "word_id";
    private ArrayAdapter<String> arrayAdapter;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    private static final String TEXT_SEARCH = "words";

    private String result;


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
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupSphinxRecognizerTask(this).execute();
    }


    /*----------------------------------------------------------------------------------------------
      CMU Speech Recognition Listeners
    */
    protected void setupRecognizer(File assetsDir) throws IOException {
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
    }

    public void onStopButtonClick(View view) {
        stopRecordingButton.setVisibility(View.INVISIBLE);
        startRecordingButton.setVisibility(View.VISIBLE);

        wordPresenter.saveWord();

        recognizer.stop();
        recognizer.cancel();

        /* Should we return to the game menu screen after getting the recognized word? */
        //Intent intent = new Intent(this, GameMenuActivity.class);
        // startActivity(intent);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        resultTextView.setText(text);
        result = hypothesis.getHypstr();

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

    /*----------------------------------------------------------------------------------------------
      MVP
    */

    @Override
    protected void onResume() {
        super.onResume();
        wordPresenter.setView(this);
    }

    @Override
    public String getWordId() {
        return WORD_ID;
    }





    /**
     * The result is a class variable which is set in this method:
     * @see #onPartialResult(Hypothesis)
     */
    @Override
    public String getRecognizerWordResult() {
        // return resultTextView.getText().toString();
        return result;
    }



    /*----------------------------------------------------------------------------------------------
      Activity related
    */

    private void populateListView() {
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                Const.GLIDING_OF_LIQUIDS_INVALID_LIST);
        listView.setAdapter(arrayAdapter);
    }
}
