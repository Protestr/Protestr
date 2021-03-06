package org.protestr.app.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import es.dmoral.prefs.Prefs;
import org.protestr.app.data.models.dao.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by someone on 5/07/17.
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
    public static final String PREFERENCES_NEEDS_EVENTS_SUBSCRIPTION = "needs_events_subscription";
    public static final String PREFERENDES_MUTED_EVENTS = "muted_events";

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

    public static void muteEvent(@NonNull Context context, @NonNull String eventId) {
        Set<String> mutedEvents = Prefs.with(context).getStringSet(PREFERENDES_MUTED_EVENTS, new HashSet<String>());
        mutedEvents.add(eventId);
        Prefs.with(context).putStringSet(PREFERENDES_MUTED_EVENTS, mutedEvents);
    }

    public static void unmuteEvent(@NonNull Context context, @NonNull String eventId) {
        Set<String> mutedEvents = Prefs.with(context).getStringSet(PREFERENDES_MUTED_EVENTS, new HashSet<String>());
        mutedEvents.remove(eventId);
        Prefs.with(context).putStringSet(PREFERENDES_MUTED_EVENTS, mutedEvents);
    }

    public static boolean isEventMuted(@NonNull Context context, @NonNull String eventId) {
        Set<String> mutedEvents = Prefs.with(context).getStringSet(PREFERENDES_MUTED_EVENTS, new HashSet<String>());
        return mutedEvents.contains(eventId);
    }
}
