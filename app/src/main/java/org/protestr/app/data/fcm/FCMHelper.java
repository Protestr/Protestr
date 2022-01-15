package org.protestr.app.data.fcm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.protestr.app.utils.PreferencesUtils;

import es.dmoral.prefs.Prefs;
import org.protestr.app.utils.PreferencesUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by someone on 26/06/17.
 */

public class FCMHelper {
    public static String ENTIRE_APP_TOPIC = "ENTIRE_APP";
    @SuppressWarnings("FieldCanBeLocal")
    private static String EVENT_TOPIC = "EVENT_";

    public static void subscribeToEntireAppTopic(Context context) {
        if (Prefs.with(context).readBoolean(PreferencesUtils.PREFERENCES_FIRST_BOOT, true)) {
            FirebaseMessaging.getInstance().subscribeToTopic(ENTIRE_APP_TOPIC);
            Prefs.with(context).writeBoolean(PreferencesUtils.PREFERENCES_FIRST_BOOT, false);
        }
    }

    public static void subscribeToEvent(@NonNull String eventId) {
        FirebaseMessaging.getInstance().subscribeToTopic(getEventTopic(eventId));
    }

    public static void unsubscribeFromEvent(@NonNull String eventId) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getEventTopic(eventId));
    }

    public static void subscribeToEvents(ArrayList<String> eventIds) {
        for (String eventId : eventIds) {
            subscribeToEvent(eventId);
        }
    }

    public static void clearInstance() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException ignored) {
                    // ignored
                }
            }
        }).start();
    }

    public static String getEventTopic(String eventId) {
        return EVENT_TOPIC + eventId;
    }
}
