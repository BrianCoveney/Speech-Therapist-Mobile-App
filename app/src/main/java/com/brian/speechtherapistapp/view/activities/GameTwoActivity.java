package com.brian.speechtherapistapp.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ItemData;
import com.brian.speechtherapistapp.models.Word;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.presentation.IWordPresenter;
import com.brian.speechtherapistapp.util.Const;
import com.brian.speechtherapistapp.view.IGameView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static com.brian.speechtherapistapp.util.Const.GLIDING_OF_LIQUIDS_WORDS_LIST;


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
    private static final int NUM_LIST_ITEMS = Const.CORRECT_WORDS_LIST.size();
    private static final String WORD_ID = "word_id";
    private String result;
    private String onItemClickResult;
    private Child child;
    private Child c;
    private static final int CHILD_ID = 0;
    private List<Child> childList;
    private String childEmail;
    boolean fabOn = true;
    boolean fabOff = false;

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

        childEmail = getChildFromChildLoginActivity();
        Log.i(LOG_TAG, "Child's email from login: " + childEmail);


        // Resolves 'com.mongodb.MongoException: android.os.NetworkOnMainThreadException'
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        /* Recycler view */

        DividerItemDecoration itemDecorator =
                new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        recyclerView.setHasFixedSize(true);

        ItemData itemsData[] = {
                new ItemData("Moon", R.drawable.ic_moon),
                new ItemData("Leg", R.drawable.ic_leg),
                new ItemData("Look", R.drawable.ic_glasses),
                new ItemData("Rabbit", R.drawable.ic_rabbit),
                new ItemData("Run", R.drawable.ic_run_shoes),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Help", R.drawable.ic_email_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp),
                new ItemData("Delete", R.drawable.ic_home_24dp)
        };

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setLayoutManager(layoutManager);

        snapHelper.attachToRecyclerView(recyclerView);

        adapter = new WordAdapter(this, itemsData, NUM_LIST_ITEMS, this);

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

        new FetchFromDatabaseTask().execute();
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

            String currentWord = child.getWordName();
            if (currentWord != null) {
                currentWord = child.getWordName();

                child = iChildPresenter.setWord(currentWord, result, childEmail);

                Word word = new Word(child.getWordName());
                boolean isWordMatch = word.hasMatch(word.getName(), GLIDING_OF_LIQUIDS_WORDS_LIST);

                int wordFreq = word.getFrequency();

                List<String> glidingList = new ArrayList<>();

                if (isWordMatch == true) {
                    glidingList.add(word.getName());
                }

                Log.i(LOG_TAG, "Child's newWord: " + child.getWordName());
                Log.i(LOG_TAG, "Child's currWord:: " + currentWord);
                Log.i(LOG_TAG, "Word is a match in list: " + isWordMatch);
                Log.i(LOG_TAG, "Word freq count: " + wordFreq);
                Log.i(LOG_TAG, "Word gliding list: " + glidingList.toString());

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
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


    // Listener method implementation of the WordAdapter interface
    @Override
    public void onListItemClicked(String itemClicked) {
        onItemClickResult = itemClicked;
        Log.i(LOG_TAG, "Clicked " + onItemClickResult);
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

    private String getChildFromChildLoginActivity() {
        SharedPreferences sharedPreferences =
                getSharedPreferences("email_pref_key", Activity.MODE_PRIVATE);
        childEmail = sharedPreferences.getString("email_key", "default");

        return childEmail;
    }


    private class FetchFromDatabaseTask extends AsyncTask<Void, Void, Child> {

        private ProgressDialog progressDialog = new ProgressDialog(GameTwoActivity.this);


        @Override
        protected Child doInBackground(Void... voids) {

            // Fetch the child from the db based on their email
            child = iChildPresenter.getChildWithEmail(childEmail);
            return child;
        }

        @Override
        protected void onPostExecute(Child child) {
            super.onPostExecute(child);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressDialog.dismiss();
                }
            }, 500);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Please wait");
            this.progressDialog.show();
        }
    }

    private int getRecyclerViewPosition() {
        SharedPreferences sharedPreferences =
                getSharedPreferences("pos_pref_key", Activity.MODE_PRIVATE);
        int position = sharedPreferences.getInt("pos_key", 0);
        return position;
    }


    /*----------------------------------------------------------------------------------------------
      Custom Dialog - where we record and save the word
    */
    public void showCustomDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voice recorder");

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();


        final TextView textViewDialogPrompt = dialogView.findViewById(R.id.tv_custom_dialog);
        textViewDialogPrompt.setText("Try saying " + onItemClickResult);
        textViewDialogPrompt.setVisibility(View.INVISIBLE);

        final FloatingMusicActionButton floatingMusicActionButton =
                dialogView.findViewById(R.id.custom_dialog_fab);


        floatingMusicActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingMusicActionButton.playAnimation();

                if (fabOn) {
                    textViewDialogPrompt.setVisibility(View.VISIBLE);
                    recognizer.startListening("words");
                    fabOn = false;
                } else {
                    textViewDialogPrompt.setVisibility(View.INVISIBLE);
                    wordPresenter.saveWord();
                    showToast("Saved: " + result);

                    if (recognizer != null) {
                        recognizer.stop();
                        recognizer.cancel();
                    }

                    // Scroll to the next item
                    int position = getRecyclerViewPosition();
                    recyclerView.scrollToPosition(position + 1);

                    alertDialog.dismiss();
                    fabOn = true;
                }
            }
        });

    }

}