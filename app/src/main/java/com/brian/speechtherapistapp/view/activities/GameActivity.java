package com.brian.speechtherapistapp.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
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
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;
import com.brian.speechtherapistapp.view.activities.base.SplashActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static com.brian.speechtherapistapp.util.Const.GLIDING_OF_LIQUIDS_WORDS_LIST;


public class GameActivity extends BaseActivity implements
        WordAdapter.WordAdapterClickListener,
        IGameView,
        edu.cmu.pocketsphinx.RecognitionListener {

    @Inject
    IWordPresenter wordPresenter;

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.rv_words)
    RecyclerView recyclerView;

    private static final String LOG_TAG = GameActivity.class.getSimpleName();
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
    boolean isFloatingMusicActionButtonOn = true;
    private List<String> glidingList = new ArrayList<>();
    private int wordFreq;
    private Word word = new Word();
    private HashMap<String, Integer> glidingHashMap = new HashMap<>();
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

        // When the user swipes the screen, the recycler view snaps to the next full screen item.
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

        // Setup speech recognition task
        new SetupTask(this).execute();


        // Setup database query task
        new FetchFromDatabaseTask().execute();
    }


    /*----------------------------------------------------------------------------------------------
      SetupTask class
    */
    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<GameActivity> activityReference;

        SetupTask(GameActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        // Heavy lifting runs on background thread
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

        // Publish results on the UI Thread
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                String toastMessage = "Failed to init recognizer " + result;
                Toast.makeText(activityReference.get(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
    } // end SetupTask class


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
     * This onResult method is an implementation of the pocketsphinx.RecognitionListener interface.
     * The 'hypothesis' passed in is a result from pocketsphinx.Hypothesis class.
     *
     * We check that hypothesis is not null, setting it equal to our string 'result' if not.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String result = hypothesis.getHypstr();

            // We get the current word from the child that has been fetched from the database
            if (child != null) {
                String currentWord = child.getWordName();

                // We pass the current word, result of the recording and the email identifier.
                // This will update the child's current word to the new word, i.e result
                child = iChildPresenter.setWord(currentWord, result, childEmail);

                // We set our word object name equal to this child's new word
                word.setName(child.getWordName());
                // Our boolean is true if there's a match between the word and the gliding list
                boolean isWordMatch = word.hasMatch(word.getName(), GLIDING_OF_LIQUIDS_WORDS_LIST);

                String newWord = word.getName();

                // Then newWord matches one of the gliding words list
                if (isWordMatch == true) {

                    // Our HashMap already contains the newWord, so the value (count) is incremented
                    if (glidingHashMap.containsKey(newWord)) {
                        glidingHashMap.put(newWord, glidingHashMap.get(newWord) + 1);
                    }
                    // Our HashMap doesn't contain the newWord, so we add the word with a value of 1
                    else {
                        glidingHashMap.put(newWord, 1);
                    }
                    // We update the Gliding of Liquids HashMap in the DB
                    iChildPresenter.setGlidingWordsMap(glidingHashMap, childEmail);
                }
            } else {
                showLongToast("Please login or create an account");
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        Intent intent = new Intent(GameActivity.this, SplashActivity.class);
                        startActivity(intent);
                    }
                }, 2000);
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
        showCustomDialogVoiceRecorder();
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
      FetchFromDatabaseTask class
      A database query can be a time consuming task and should not be run on the main thread
    */
    private class FetchFromDatabaseTask extends AsyncTask<Void, Void, Child> {

        private ProgressDialog progressDialog = new ProgressDialog(GameActivity.this);

        // Heavy lifting runs on background thread
        @Override
        protected Child doInBackground(Void... voids) {
            // Fetch the child from the db based on their email address.
            // This email was retrieved by getChildFromLoginActivity()
            child = iChildPresenter.getChildWithEmail(childEmail);
            return child;
        }

        // Publish results on the UI Thread
        @Override
        protected void onPostExecute(Child child) {
            super.onPostExecute(child);

            // Close the progressDialog once the database query has returned a result.
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

            // We create a spinning progress dialog to let the user know that our app is still
            // running, while information is retrieved from the database.
            this.progressDialog.setMessage("Please wait");
            this.progressDialog.show();
        }
    } // end FetchFromDatabaseTask class



    /*----------------------------------------------------------------------------------------------
      Custom Dialog - where we record and save the word
    */
    public void showCustomDialogVoiceRecorder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voice recorder");

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.yellow_light);
        alertDialog.getWindow().getAttributes().verticalMargin = 0.3F;
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

                if (isFloatingMusicActionButtonOn) {
                    textViewDialogPrompt.setVisibility(View.VISIBLE);
                    recognizer.startListening("words");
                    isFloatingMusicActionButtonOn = false;
                } else {
                    textViewDialogPrompt.setVisibility(View.INVISIBLE);
                    wordPresenter.saveWord();
                    floatingMusicActionButton.playAnimation();
                    showToast("You said: " + result);

                    if (recognizer != null) {
                        recognizer.stop();
                        recognizer.cancel();
                    }

                    // Scroll to the next item
//                    int position = getRecyclerViewPosition();
//                    recyclerView.scrollToPosition(position + 1);

                    alertDialog.dismiss();

                    Log.i(LOG_TAG, "Item: " + onItemClickResult);
                    Log.i(LOG_TAG, "Result: " + result);

                    if (child != null) {
                        if (result.matches(onItemClickResult)) {
                            showCustomDialogTrophy();
                        } else {
                            showCustomDialogTryAgain();
                        }
                    }

                    isFloatingMusicActionButtonOn = true;
                }
            }
        });
    }


    public void showCustomDialogTrophy() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_trophy, null);
        builder.setView(dialogView);

        final AlertDialog closedialog= builder.create();

        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
            }
        }, 3000); // the timer will count 5 seconds....
    }

    public void showCustomDialogTryAgain() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_try_again, null);
        builder.setView(dialogView);

        final AlertDialog closedialog= builder.create();

        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
            }
        }, 3000); // the timer will count 5 seconds....
    }

    /*----------------------------------------------------------------------------------------------
      SharedPreferences
    */


    /**
     * When the activity has started, this method is called in the onCreate() method. We fetch the
     * email entered in the LoginActivity using the SharedPreferences object, and pass it to our
     * 'childEmail' instance variable.
     *
     * @link LocationActivity#onClickButtonLoginChild()
     * @see GameActivity#onCreate(Bundle)
     * @return String
     */
    private String getChildFromChildLoginActivity() {

        // We create a SharedPreferences object so that we can retrieve key-value data from the
        // LoginActivity
        SharedPreferences sharedPreferences =
                getSharedPreferences("email_pref_key", Activity.MODE_PRIVATE);

        // We set the 'childEmail' instance variable equal to the returned value
        childEmail = sharedPreferences.getString("email_key", "default");

        return childEmail;
    }




    private int getRecyclerViewPosition() {
        SharedPreferences sharedPreferences =
                getSharedPreferences("pos_pref_key", Activity.MODE_PRIVATE);
        int position = sharedPreferences.getInt("pos_key", 0);
        return position;
    }
}
