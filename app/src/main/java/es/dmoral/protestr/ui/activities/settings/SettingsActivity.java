package es.dmoral.protestr.ui.activities.settings;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.ui.UpdatePool;
import es.dmoral.protestr.ui.fragments.events.EventsFragment;
import es.dmoral.protestr.utils.LocaleUtils;
import es.dmoral.protestr.utils.PreferencesUtils;

/**
 * A {@link PreferenceActivity} that presents a set of application general_settings. On
 * handset devices, general_settings are presented as a single list. On tablets,
 * general_settings are split by category, with category headers shown to the left of
 * the list of general_settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            final String stringValue = value.toString();

            switch (preference.getKey()) {
                case PreferencesUtils.PREFERENCES_FILTER_LOCATION_EVENTS:
                case PreferencesUtils.PREFERENCES_SELECTED_COUNTRY:
                    UpdatePool.needsUpdates(EventsFragment.class.toString());
            }

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(listPreference.getEntries()[index]);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        // See http://stackoverflow.com/a/14304020/4208583
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new GeneralPreferenceFragment(), GeneralPreferenceFragment.class.getName())
                    .commit();
        }

        setupActionBar();
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #onPreferenceChangeListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        final String key = preference.getKey();
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        // Trigger the listener immediately with the preference's
        // current value.
        switch (key) {
            case PreferencesUtils.PREFERENCES_FILTER_LOCATION_EVENTS:
                onPreferenceChangeListener.onPreferenceChange(preference,
                        Prefs.with(preference.getContext()).read(key));
                break;
            case PreferencesUtils.PREFERENCES_SELECTED_COUNTRY:
                onPreferenceChangeListener.onPreferenceChange(preference,
                        Prefs.with(preference.getContext()).read(key, LocaleUtils.getDeviceLocale(preference.getContext())));
                break;
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.general_settings);

            bindPreferenceSummaryToValue(findPreference(PreferencesUtils.PREFERENCES_FILTER_LOCATION_EVENTS));
            inflateCountrySelection();
        }

        private void inflateCountrySelection() {
            ListPreference countrySelectionPreference = (ListPreference) findPreference(PreferencesUtils.PREFERENCES_SELECTED_COUNTRY);
            countrySelectionPreference.setEntries(LocaleUtils.getAvailableLocales());
            countrySelectionPreference.setEntryValues(LocaleUtils.getAvailableIso3Codes());
            countrySelectionPreference.setDefaultValue(LocaleUtils.getDeviceLocale(countrySelectionPreference.getContext()));
            countrySelectionPreference.setValueIndex(countrySelectionPreference.findIndexOfValue(Prefs.with(countrySelectionPreference.getContext()).read(countrySelectionPreference.getKey(), LocaleUtils.getDeviceLocale(countrySelectionPreference.getContext()))));
            bindPreferenceSummaryToValue(countrySelectionPreference);
        }
    }
}
