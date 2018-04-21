package org.protestr.app.data.fcm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

import org.protestr.app.utils.PreferencesUtils;

import es.dmoral.prefs.Prefs;
import org.protestr.app.utils.PreferencesUtils;

/**
 * Created by someone on 26/06/17.
 */

public class FCMHelper {
    public static String ENTIRE_APP_TOPIC = "ENTIRE_APP";
    @SuppressWarnings("FieldCanBeLocal")
    private static String EVENT_TOPIC = "EVENT_";

    public static void subscribeToFCMTopic(Context context, @NonNull String topic) {
        if (Prefs.with(context).readBoolean(PreferencesUtils.PREFERENCES_FIRST_BOOT, true)) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            Prefs.with(context).writeBoolean(PreferencesUtils.PREFERENCES_FIRST_BOOT, false);
        }
    }

    public static String getEventTopic(int eventId) {
        return EVENT_TOPIC + eventId;
    }
}
