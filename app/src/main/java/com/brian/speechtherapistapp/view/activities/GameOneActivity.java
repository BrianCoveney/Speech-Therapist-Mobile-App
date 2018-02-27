package com.brian.speechtherapistapp.view.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.util.Const;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class GameOneActivity extends BaseActivity implements
        RecognitionListener {

    private String listItemClicked;
    private ArrayAdapter<String> arrayAdapter;
    private List words = Const.GLIDING_OF_LIQUIDS_VALID;

    @BindView(R.id.game_one_list_view)
    ListView listView;

    @BindView(R.id.caption_text_2)
    TextView captionTextView;

    @BindView(R.id.result_text_2)
    TextView resultTextView;

    @BindView(R.id.start_recording_button)
    Button startRecordingButton;

    private final String LOG_TAG = GameOneActivity.class.getSimpleName();

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "recording on";

    private SpeechRecognizer recognizer;
    private HashMap<String, String> captions;


    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";
    private static final String TEXT_SEARCH = "words";




    @Override
    protected int getContentView() {
        return R.layout.activity_game_one;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        populateListView();

        onListItemClickListener();

        // Prepare the data for UI
        captions = new HashMap<>();
        captions.put(KWS_SEARCH, "To Start recording say recording on");
        captions.put(MENU_SEARCH, "Say words");
        captions.put(TEXT_SEARCH, "Say something like telephone");

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
                activityReference.get().switchSearch(KWS_SEARCH);
            }
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create grammar-based search for custom recognition
        File testGrammar = new File(assetsDir, "words-gliding-of-liquids.gram");
        recognizer.addGrammarSearch(TEXT_SEARCH, testGrammar);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);


        // Fails
        // recognizer.startListening(onListItemClickListener());

        // Fails
        // recognizer.startListening("telephone");

        String caption = captions.get(searchName);
        captionTextView.setText(caption);
    }

    @Override
    public void onBeginningOfSpeech() { }

    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals(TEXT_SEARCH))
            switchSearch(TEXT_SEARCH);
        else
            resultTextView.setText(text);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            showToast(text);
        }
    }

    @Override
    public void onError(Exception error) {
        captionTextView.setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    private void populateListView() {
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, words);
        listView.setAdapter(arrayAdapter);
    }

    private void onListItemClickListener() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object itemClicked = listView.getItemAtPosition(position);
                showToast(itemClicked.toString());
                listItemClicked = itemClicked.toString();
                Log.i(LOG_TAG, listItemClicked);
            }

        });

        // unavailable
//        Log.i(LOG_TAG, "Here: " + listItemClicked);

    }



    @OnClick(R.id.start_recording_button)
    public void onStartRecordingButtonClick() {

    }
}
