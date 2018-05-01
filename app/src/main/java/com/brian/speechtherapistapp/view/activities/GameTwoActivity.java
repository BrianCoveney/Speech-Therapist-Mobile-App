package com.brian.speechtherapistapp.view.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.presentation.IWordPresenter;
import com.brian.speechtherapistapp.repository.DBController;
import com.brian.speechtherapistapp.util.Const;
import com.brian.speechtherapistapp.view.IGameView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class GameTwoActivity extends BaseActivity implements
        WordAdapter.WordAdapterClickListener,
        IGameView,
        edu.cmu.pocketsphinx.RecognitionListener {

    @Inject
    IWordPresenter wordPresenter;

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.rv_words)
    RecyclerView recyclerView;

    private static final String LOG_TAG = GameTwoActivity.class.getSimpleName();
    private WordAdapter adapter;
    private static final int NUM_LIST_ITEMS = Const.LIST_OF_CORRECT_WORDS.size();
    private static final String WORD_ID = "word_id";
    private String result;
    private String onItemClickResult;
    private Child child;
    private static final int CHILD_ID = 0;
    private List<Child> childList;



    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private static final String TEXT_SEARCH = "words";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);
        getLayoutInflater().inflate(R.layout.activity_game_two, frameLayout);
        ButterKnife.bind(this);

        // Resolves 'com.mongodb.MongoException: android.os.NetworkOnMainThreadException'
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        child = DBController.getInstance().getChildFromDB(CHILD_ID);

        /* Recycler view */

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new WordAdapter(NUM_LIST_ITEMS, this);
        recyclerView.setAdapter(adapter);


        /* Speech Recognition */

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
            } else {
                activityReference.get().reset(TEXT_SEARCH);
            }
        }
    }

    private void reset(String searchName) {
        recognizer.stop();
        recognizer.startListening(searchName);
    }

    /*----------------------------------------------------------------------------------------------
      CMU Speech Recognition Listeners
    */
    protected void setupRecognizer(File assetsDir) throws IOException {
        try {
            recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                    .setRawLogDir(assetsDir)
                    .getRecognizer();
            recognizer.addListener(this);

            // Create grammar-based search for custom recognition
            File testGrammar = new File(assetsDir, "text.gram");
            recognizer.addGrammarSearch(TEXT_SEARCH, testGrammar);
        } catch (Exception e) {
            e.getMessage();
        }
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
            String result = hypothesis.getHypstr();

            child = DBController.getInstance().getChildFromDB(CHILD_ID);
            String currentWord = child.getWord();
            if (currentWord != null) {
                currentWord = child.getWord();
                Log.i(LOG_TAG, "Child's currWord:: " + currentWord);

                DBController.getInstance().setWord(child, currentWord, result);
                Log.i(LOG_TAG, "Child's newWord: " + child.getWord());

                showToast("Saved: " + result);
            } else {
                showToast("You need to create a user account first!");
            }

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
        Log.i(LOG_TAG,"Clicked " + onItemClickResult);
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
      Custom Dialog - where we record and save the word
    */
    public void showCustomDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Say: " + onItemClickResult);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final Button saveButtonDialog = dialogView.findViewById(R.id.save_recording_button_dialog);
        saveButtonDialog.setVisibility(View.INVISIBLE);

        final Button startButtonDialog = dialogView.findViewById(R.id.btn_dialog_start_recording);
        startButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizer.startListening("words");
                startButtonDialog.setVisibility(View.INVISIBLE);
                saveButtonDialog.setVisibility(View.VISIBLE);
                reset(TEXT_SEARCH);
            }
        });

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wordPresenter.saveWord();

                if (recognizer != null) {
                    recognizer.stop();
                    recognizer.cancel();
                }

                alertDialog.dismiss();
            }
        });

    }

}