package com.example.android.newsappstage2project;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.title_activity_settings));
    }
    @Override
    public void onBackPressed() {
        //preferences has changed, back button acts as up button
        NavUtils.navigateUpFromSameTask(this);
    }
    public static class PreferenceSettings extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //inflate layout preference
            addPreferencesFromResource(R.xml.settings_preference);
            //news per page preference
            String preferenceNewsPerPageKey = getString(R.string.settings_news_per_page_key);
            Preference newsPerPagePreference = findPreference(preferenceNewsPerPageKey);
            setListenerAndBind(newsPerPagePreference);
            //news per row preference
            String preferenceNewsPerRowKey = getString(R.string.settings_news_per_row_key);
            Preference newsPerRowPreference = findPreference(preferenceNewsPerRowKey);
            setListenerAndBind(newsPerRowPreference);
            //search results per page preference
            String preferenceResultsPerPageKey = getString(R.string.settings_results_per_page_key);
            Preference resultsPerPagePreference = findPreference(preferenceResultsPerPageKey);
            setListenerAndBind(resultsPerPagePreference);
            //search results order by preference
            String preferenceOrderByKey = getString(R.string.settings_order_by_key);
            Preference orderByPreference = findPreference(preferenceOrderByKey);
            setListenerAndBind(orderByPreference);
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
            return true;
        }
        /**
         * Set listener on the preference and bind default/custom value
         *
         * @param preference the preference on which to set the listener
         */
        private void setListenerAndBind(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext());
            if (preference instanceof ListPreference) {
                String preferenceString = "";
                String preferenceKey = preference.getKey();
                if (preferenceKey.equals(getString(R.string.settings_news_per_page_key))) {
                    preferenceString = preferences.getString(preferenceKey,
                            getString(R.string.settings_news_per_page_default));
                }
                if (preferenceKey.equals(getString(R.string.settings_news_per_row_key))) {
                    preferenceString = preferences.getString(preferenceKey,
                            getString(R.string.settings_news_per_row_default));
                }
                if (preferenceKey.equals(getString(R.string.settings_results_per_page_key))) {
                    preferenceString = preferences.getString(preferenceKey,
                            getString(R.string.settings_results_per_page_default));
                }
                if (preferenceKey.equals(getString(R.string.settings_order_by_key))) {
                    preferenceString = preferences.getString(preferenceKey,
                            getString(R.string.settings_order_by_default));
                }
                onPreferenceChange(preference, preferenceString);
            }
        }
    }
}
