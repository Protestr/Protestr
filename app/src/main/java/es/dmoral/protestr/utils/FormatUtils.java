package es.dmoral.protestr.utils;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by grender on 16/06/17.
 */

public class FormatUtils {
    public static String addLeadingZero(int number) {
        return String.format(Locale.ENGLISH, "%02d", number);
    }

    public static String formatDateByDefaultLocale(long timeInMilliseconds) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        return dateFormat.format(timeInMilliseconds);
    }
}
