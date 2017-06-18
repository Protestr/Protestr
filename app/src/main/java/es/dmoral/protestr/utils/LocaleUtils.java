package es.dmoral.protestr.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Created by grender on 15/02/17.
 */

public class LocaleUtils {

    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    private static String[] filteredLocales;
    private static String[] filteredIso3Codes;
    private static HashMap<String, String> filteredIso3Locales;

    public static Locale[] getRawLocales() {
        return LOCALES;
    }

    public static String[] getAvailableLocales() {
        if (filteredLocales == null) {
            filteredLocales = getAvailableIso3Locales().values().toArray(new String[getAvailableIso3Locales().size()]);
        }
        return filteredLocales;
    }

    public static String[] getAvailableIso3Codes() {
        if (filteredIso3Codes == null)
            filteredIso3Codes = getAvailableIso3Locales().keySet().toArray(new String[getAvailableIso3Locales().size()]);
        return filteredIso3Codes;
    }

    public static HashMap<String, String> getAvailableIso3Locales() {
        if (filteredIso3Locales == null)
            initIso3Locales();
        return filteredIso3Locales;
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
        filteredIso3Locales = sortHashMapByValues(filteredIso3Locales);
    }

    public static String getDeviceLocale(@NonNull Context context) {
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return new Locale("", telephonyManager.getNetworkCountryIso()).getISO3Country();
    }

    public static String iso2ToIso3(String iso2) {
        return new Locale("", iso2).getISO3Country();
    }

    private static LinkedHashMap<String, String> sortHashMapByValues(HashMap<String, String> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();

        for (String val : mapValues) {
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String comp1 = passedMap.get(key);

                if (comp1.equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
