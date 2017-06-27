package es.dmoral.protestr.fcm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.utils.Constants;

/**
 * Created by grender on 26/06/17.
 */

public class FCMHelper {
    public static String ENTIRE_APP_TOPIC = "ENTIRE_APP";
    @SuppressWarnings("FieldCanBeLocal")
    private static String EVENT_TOPIC = "EVENT_";

    public static void subscribeToFCMTopic(Context context, @NonNull String topic) {
        if (Prefs.with(context).readBoolean(Constants.PREFERENCES_FIRST_BOOT, true)) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            Prefs.with(context).writeBoolean(Constants.PREFERENCES_FIRST_BOOT, false);
        }
    }

    public static String getEventTopic(int eventId) {
        return EVENT_TOPIC + eventId;
    }
}
