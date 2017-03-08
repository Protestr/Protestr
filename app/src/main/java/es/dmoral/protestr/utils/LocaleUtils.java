package es.dmoral.protestr.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Created by grender on 15/02/17.
 */

public class LocaleUtils {
    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    private static ArrayList<String> filteredLocales;
    private static ArrayList<String> filteredIso3Codes;
    private static HashMap<String, String> filteredIso3Locales;

    public static Locale[] getRawLocales() {
        return LOCALES;
    }

    public static ArrayList<String> getAvailableLocales() {
        if (filteredLocales == null)
            initLocales(true);
        return filteredLocales;
    }

    public static HashMap<String, String> getAvailableIso3Locales() {
        if (filteredIso3Locales == null)
            initIso3Locales();
        return filteredIso3Locales;
    }

    public static ArrayList<String> getAvailableIso3Codes() {
        if (filteredIso3Codes == null)
            initIso3Codes();
        return filteredIso3Codes;
    }

    private static void initLocales(boolean iso3safe) {
        filteredLocales = new ArrayList<>();
        for (Locale locale : LOCALES) {
            final String country = locale.getDisplayCountry();
            if (iso3safe) {
                try {
                    locale.getISO3Country();
                } catch (MissingResourceException mre) {
                    continue;
                }
            }
            if (country.length() > 0 && !filteredLocales.contains(country))
                filteredLocales.add(country);
        }
        Collections.sort(filteredLocales);
    }

    private static void initIso3Codes() {
        filteredIso3Codes = new ArrayList<>();
        for (Locale locale : LOCALES) {
            final String iso3Locale;
            try {
                iso3Locale = locale.getISO3Country();
            } catch (MissingResourceException mre) {
                continue;
            }
            if (locale.getDisplayCountry().length() > 0 && ! filteredIso3Codes.contains(iso3Locale))
            filteredIso3Codes.add(iso3Locale);
        }
    }

    private static void initIso3Locales() {
        filteredIso3Locales = new HashMap<>();
        for (Locale locale : LOCALES) {
            final String country = locale.getDisplayCountry();
            if (country.length() == 0)
                continue;
            final String iso3Locale;
            try {
                iso3Locale = locale.getISO3Country();
            } catch (MissingResourceException mre) {
                continue;
            }
            filteredIso3Locales.put(iso3Locale, country);
        }
    }

    public static String getDeviceLocale(@NonNull Context context) {
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return new Locale("", telephonyManager.getNetworkCountryIso()).getISO3Country();
    }
}
