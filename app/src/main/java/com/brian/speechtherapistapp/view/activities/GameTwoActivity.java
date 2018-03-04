package com.brian.speechtherapistapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

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
        wordPresenter.saveWord();
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
}
