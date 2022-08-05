package ostrozlik.adam.simplerecorder.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ostrozlik.adam.simplerecorder.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}