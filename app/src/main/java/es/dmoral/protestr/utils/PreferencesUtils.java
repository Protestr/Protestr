package es.dmoral.protestr.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.models.models.User;

/**
 * Created by grender on 5/07/17.
 */

public class PreferencesUtils {
    public static final String PREFERENCES_FIRST_BOOT = "first_boot";
    public static final String PREFERENCES_LOGGED_IN = "logged_in";
    public static final String PREFERENCES_USER_NAME = "username";
    public static final String PREFERENCES_PROFILE_PIC_URL = "profile_pic_url";
    public static final String PREFERENCES_USER_ID = "user_id";
    public static final String PREFERENCES_EMAIL = "email";
    public static final String PREFERENCES_PASSWORD = "password";
    public static final String PREFERENCES_FILTER_LOCATION_EVENTS = "filter_location_events";
    public static final String PREFERENCES_SELECTED_COUNTRY = "selected_country";
    public static final String PREFERENCES_ORDER_BY = "order_by";
    public static final String PREFERENCES_ALERT_ENABLED = "alert_enabled";
    public static final String PREFERENCES_SELECTED_CONTACT_NAME = "selected_contact_name";
    public static final String PREFERENCES_SELECTED_CONTACT_NUMBER = "selected_contact_number";
    public static final String PREFERENCES_SMS_MESSAGE = "sms_message";
    public static final String PREFERENCES_SHAKE_NUMBER = "shake_number";
    public static final String PREFERENCES_SENSOR_SENSITIVITY = "sensor_sensitivity";
    public static final String PREFERENCES_TIME_TO_RESTART = "time_to_restart";

    public static void storeLoggedUser(@NonNull Context context, User user) {
        Prefs.with(context).write(PREFERENCES_USER_ID, user.getId());
        Prefs.with(context).write(PREFERENCES_USER_NAME, user.getUsername());
        Prefs.with(context).write(PREFERENCES_EMAIL, user.getEmail());
        Prefs.with(context).write(PREFERENCES_PASSWORD, user.getPassword());
        Prefs.with(context).write(PREFERENCES_PROFILE_PIC_URL, user.getProfilePicUrl());
    }

    public static User getLoggedUser(@NonNull Context context) {
        return new User(
            Prefs.with(context).read(PREFERENCES_USER_ID),
            Prefs.with(context).read(PREFERENCES_EMAIL),
            Prefs.with(context).read(PREFERENCES_USER_NAME),
            Prefs.with(context).read(PREFERENCES_PASSWORD),
            Prefs.with(context).read(PREFERENCES_PROFILE_PIC_URL)
        );
    }
}
