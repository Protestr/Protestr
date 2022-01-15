package org.protestr.app.utils;

import java.util.Calendar;

/**
 * Created by someone on 17/06/17.
 */

public class TimeUtils {
    public static long getTimeInMillis(int year, int month, int dayOfMonth) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.set(Calendar.YEAR, year);
        auxCalendar.set(Calendar.MONTH, month);
        auxCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        auxCalendar.clear(Calendar.HOUR_OF_DAY);
        auxCalendar.clear(Calendar.MINUTE);
        auxCalendar.clear(Calendar.SECOND);
        auxCalendar.clear(Calendar.MILLISECOND);
        return auxCalendar.getTimeInMillis();
    }

    public static long getTimeInMillis(int year, int month, int dayOfMonth, int hourOfDay, int minutes) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.set(Calendar.YEAR, year);
        auxCalendar.set(Calendar.MONTH, month);
        auxCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        auxCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        auxCalendar.set(Calendar.MINUTE, minutes);
        auxCalendar.clear(Calendar.SECOND);
        auxCalendar.clear(Calendar.MILLISECOND);
        return auxCalendar.getTimeInMillis();
    }

    public static long getCurrentTimeInMillis() {
        final Calendar auxCalendar = Calendar.getInstance();
        return auxCalendar.getTimeInMillis();
    }

    public static long getCurrentTimeInMillisStartingFromMinutes() {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.clear(Calendar.SECOND);
        auxCalendar.clear(Calendar.MILLISECOND);
        return auxCalendar.getTimeInMillis();
    }

    public static int getYearFromMillis(long timeInMillis) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.setTimeInMillis(timeInMillis);
        return auxCalendar.get(Calendar.YEAR);
    }

    public static int getMonthFromMillis(long timeInMillis) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.setTimeInMillis(timeInMillis);
        return auxCalendar.get(Calendar.MONTH);
    }

    public static int getDayOfMonthFromMillis(long timeInMillis) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.setTimeInMillis(timeInMillis);
        return auxCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHourOfDayFromMillis(long timeInMillis) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.setTimeInMillis(timeInMillis);
        return auxCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteFromMillis(long timeInMillis) {
        final Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.setTimeInMillis(timeInMillis);
        return auxCalendar.get(Calendar.MINUTE);
    }
}
