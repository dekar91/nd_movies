package com.dekar.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        android.support.v7.preference.PreferenceScreen prefScreen = getPreferenceScreen();

        int count = prefScreen.getPreferenceCount();

        for (int i = 0; i < count; i++)
        {
            android.support.v7.preference.Preference  p = prefScreen.getPreference(i);
            String value = sharedPreferences.getString(p.getKey(), "");
            setPreferanceSummary(p, value);

        }
    }

    private void setPreferanceSummary(android.support.v7.preference.Preference p, String value)
    {
        if (p instanceof android.support.v7.preference.ListPreference)
        {
            android.support.v7.preference.ListPreference lpref = (android.support.v7.preference.ListPreference) p;
            int prefIndex = lpref.findIndexOfValue(value);

            if(prefIndex >= 0)
            {
                lpref.setSummary(lpref.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        android.support.v7.preference.Preference pref = findPreference(s);

        if(null != pref)
        {
            String value = sharedPreferences.getString(s, "");
            setPreferanceSummary(pref, value);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
