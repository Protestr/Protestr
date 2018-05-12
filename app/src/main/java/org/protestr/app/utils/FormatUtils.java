package org.protestr.app.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by someone on 16/06/17.
 */

public class FormatUtils {
    public static String addLeadingZero(int number) {
        return String.format(Locale.ENGLISH, "%02d", number);
    }

    public static String formatDateByDefaultLocale(long timeInMilliseconds) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        return dateFormat.format(timeInMilliseconds);
    }

    public static String formatHours(long timeInMilliseconds) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        return dateFormat.format(timeInMilliseconds);
    }

    public static String formatEventUpdate(long timeInMilliseconds) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm a", Locale.getDefault());
        return dateFormat.format(timeInMilliseconds);
    }

    public static boolean isValidEmailFormat(CharSequence email) {
        return !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
