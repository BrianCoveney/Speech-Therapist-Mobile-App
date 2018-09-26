package com.brian.speechtherapistapp.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.brian.speechtherapistapp.R;

public class MyPreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBoxPreference locationPreference, darkThemePreference, lightThemePreference;

    private CustomListener customListener;

    public interface CustomListener {
        void togglePeriodicLocUpdates();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        locationPreference = (CheckBoxPreference) findPreference("location_preference");
        darkThemePreference = (CheckBoxPreference) findPreference("dark_preference");
        lightThemePreference = (CheckBoxPreference) findPreference("light_preference");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        locationPreference.setChecked(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        customListener = (PreferenceActivity) context;
    }

    //Configure CheckboxPreferences to work like RadioButtons
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String key = preference.getKey();

        if(key.equals(null)){
            Toast.makeText(getActivity(), "not changed", Toast.LENGTH_SHORT).show();
            locationPreference.setChecked(false);
        }

        else if (key.equals("location_preference")) {
            // Switch location on/off
            customListener.togglePeriodicLocUpdates();

        }
        else if (key.equals("dark_preference")) {
            SharedPreferences sharedPref = this.getActivity()
                    .getSharedPreferences("dark_pref_key", Activity.MODE_PRIVATE);

            // We write to a SharedPreference file by passing the key: 'email_key' and value: 'email'
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("dark_pref_key", true);
            editor.apply();

            Toast.makeText(getActivity(), "dark theme", Toast.LENGTH_SHORT).show();
            lightThemePreference.setChecked(false);
        }
        else if (key.equals("light_preference")) {
            SharedPreferences sharedPref = this.getActivity()
                    .getSharedPreferences("light_pref_key", Activity.MODE_PRIVATE);

            // We write to a SharedPreference file by passing the key: 'email_key' and value: 'email'
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("light_pref_key", true);
            editor.apply();

            Toast.makeText(getActivity(), "light theme", Toast.LENGTH_SHORT).show();
            darkThemePreference.setChecked(false);
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

}
