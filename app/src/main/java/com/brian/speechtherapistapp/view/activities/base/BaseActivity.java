package com.brian.speechtherapistapp.view.activities.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.view.activities.HomeActivity;
import com.brian.speechtherapistapp.view.activities.googlemaps.MapsActivity;
import com.brian.speechtherapistapp.view.activities.PreferenceActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected String[] mTaskItems;
    protected DrawerLayout mDrawerLayout;
    protected FrameLayout frameLayout;
    protected ListView mDrawerList;
    protected ImageView navImages;
    protected ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        frameLayout = findViewById(R.id.content_frame);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addDrawerItems();
        setupDrawer();

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    protected void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the ActionBar
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_closed) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Items in Actionbar are pressed (not the Hamburger menu)
        switch (item.getItemId()) {
            case R.id.action_googlemaps:
                Intent intentMap = new Intent(this, MapsActivity.class);
                startActivity(intentMap);
                break;
            case R.id.action_login:
                Intent intentHome = new Intent(this, HomeActivity.class);
                startActivity(intentHome);
            default:
                break;
        }

        // Activate the navigation drawer toggle (Hamburger menu)
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    private void addDrawerItems(){
        mTaskItems = getResources().getStringArray(R.array.menu_items);
        mDrawerList = (ListView) findViewById(R.id.lv_nav_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navImages = (ImageView) findViewById(R.id.iv_nav_drawer_icons);

        View headerView = View.inflate(this, R.layout.nav_header, null);
        mDrawerList.addHeaderView(headerView);

        CustomAdapter customAdapter = new CustomAdapter(this, mTaskItems);
        mDrawerList.setAdapter(customAdapter);

        // when items in Navigation Drawer are pressed
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1:
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        Intent intentSettings = new Intent(getApplicationContext(), PreferenceActivity.class);
                        startActivity(intentSettings);
                        break;
                    case 3:
                        Intent intentMap = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intentMap);
                        break;
                }
            }
        });
    }
}
