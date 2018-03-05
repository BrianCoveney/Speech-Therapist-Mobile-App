package com.brian.speechtherapistapp.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IWordPresenter;
import com.brian.speechtherapistapp.view.IGameView;

import javax.inject.Inject;

import butterknife.BindView;


public class GameTwoActivity extends BaseActivity
        implements WordAdapter.WordAdapterClickListener, IGameView {

    @Inject
    IWordPresenter wordPresenter;

    @BindView(R.id.rv_words)
    RecyclerView recyclerView;


    private WordAdapter adapter;
    private static final int NUM_LIST_ITEMS = 14;
    private static final String WORD_ID = "word_id";
    private String result;

    String test = "myTest";


    @Override
    protected int getContentView() {
        return R.layout.activity_game_two;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        ((MainApplication)getApplication()).getPresenterComponent().inject(this);


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
        result = itemClicked;
        showToast("Saved " + result);

        // Persist word to the db
        wordPresenter.saveWord();

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
        builder.setTitle("Play Game");
        builder.setMessage("Say: " + result);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert, null);
        builder.setView(dialogView);

        final Button saveButtonDialog = dialogView.findViewById(R.id.save_recording_button_dialog);
        saveButtonDialog.setVisibility(View.INVISIBLE);

        final Button startButtonDialog = dialogView.findViewById(R.id.start_recording_button_dialog);
        startButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButtonDialog.setVisibility(View.INVISIBLE);
                saveButtonDialog.setVisibility(View.VISIBLE);
                showToast(test);
            }
        });

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("saved");
            }
        });

        builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Ok clicked");
            }
        });


        builder.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Cancel clicked");
                // Dialog canceled by default.
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void test() {
        showToast(getWordId());
    }

}
