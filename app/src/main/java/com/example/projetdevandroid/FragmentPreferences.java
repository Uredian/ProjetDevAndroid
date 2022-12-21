package com.example.projetdevandroid;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class FragmentPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.mes_preferences, rootKey);
    }

}
