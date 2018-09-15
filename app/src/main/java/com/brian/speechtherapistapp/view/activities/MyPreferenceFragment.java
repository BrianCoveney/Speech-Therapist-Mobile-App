package com.brian.speechtherapistapp.view.activities;

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

    private CheckBoxPreference locationPreference;

    private Toggle toggle;

    public interface Toggle {
        void togglePeriodicLocUpdates();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        locationPreference = (CheckBoxPreference) findPreference("location_preference");
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
        toggle = (PreferenceActivity) context;
    }

    //Configure CheckboxPreferences to work like RadioButtons
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String key = preference.getKey();

        if(key.equals(null)){
            String none = "not changed";
            Toast.makeText(getActivity(), none, Toast.LENGTH_SHORT).show();
            locationPreference.setChecked(false);
        }

        else if (key.equals("location_preference")) {
            // Switch location on/off
            toggle.togglePeriodicLocUpdates();

        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

}
