package es.dmoral.protestr.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by grender on 5/06/17.
 */

public class SmsUtils {
    public static void sendSMS(Context context, @NonNull String phoneNumber, @NonNull String message) {
        try {
            final SmsManager smsManager = SmsManager.getDefault();
            // useless?
            final PendingIntent sentPendingIntent = PendingIntent.getBroadcast(
                    context, 0, new Intent("SMS_SENT"), 0
            );
            smsManager.sendTextMessage(phoneNumber, null, message, sentPendingIntent, null);
            Log.d("Protestr-SMS", "SMS Sent");
        } catch (Exception ignored) {
            // ignored
            Log.d("Protestr-SMS", "SMS Error");
        }
    }
}
