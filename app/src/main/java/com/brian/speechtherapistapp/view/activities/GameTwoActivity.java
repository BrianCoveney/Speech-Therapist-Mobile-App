package com.brian.speechtherapistapp.view.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IWordPresenter;
import com.brian.speechtherapistapp.view.IGameView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class GameTwoActivity extends BaseActivity
        implements WordAdapter.WordAdapterClickListener, IGameView, edu.cmu.pocketsphinx.RecognitionListener {

    @Inject
    IWordPresenter wordPresenter;

    @BindView(R.id.rv_words)
    RecyclerView recyclerView;

    private static final String LOG_TAG = GameTwoActivity.class.getSimpleName();
    private WordAdapter adapter;
    private static final int NUM_LIST_ITEMS = 14;
    private static final String WORD_ID = "word_id";
    private String result;
    private String onItemClickResult;


    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    private static final String TEXT_SEARCH = "words";


    @Override
    protected int getContentView() {
        return R.layout.activity_game_two;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);


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
        new SetupTask(this).execute();


        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new WordAdapter(NUM_LIST_ITEMS, this);
        recyclerView.setAdapter(adapter);
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<GameTwoActivity> activityReference;

        SetupTask(GameTwoActivity activity) {
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

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;
        result = hypothesis.getHypstr();
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            showToast("Saved: " + text);
            Log.i(LOG_TAG, "onResult: " + text);
        }
    }

    @Override
    public void onError(Exception error) {
    }

    @Override
    public void onTimeout() {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_refresh:
                adapter = new WordAdapter(NUM_LIST_ITEMS, this);
                recyclerView.setAdapter(adapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClicked(String itemClicked) {
        onItemClickResult = itemClicked;
        showToast("Clicked " + onItemClickResult);
        showCustomDialog();
    }


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


    @Override
    public String getRecognizerWordResult() {
        return result;
    }


    /*----------------------------------------------------------------------------------------------
      Custom Dialog
    */

    public void showCustomDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Say: " + onItemClickResult);
        // builder.setMessage("Say: " + onItemClickResult);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final Button saveButtonDialog = dialogView.findViewById(R.id.save_recording_button_dialog);
        saveButtonDialog.setVisibility(View.INVISIBLE);

        final Button startButtonDialog = dialogView.findViewById(R.id.start_recording_button_dialog);
        startButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizer.startListening(TEXT_SEARCH);
                startButtonDialog.setVisibility(View.INVISIBLE);
                saveButtonDialog.setVisibility(View.VISIBLE);
            }
        });

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordPresenter.saveWord();
                recognizer.stop();
                recognizer.cancel();
                alertDialog.dismiss();
            }
        });

    }

}
