package es.dmoral.protestr.detention_alert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.SmsUtils;
import im.delight.android.location.SimpleLocation;

public class ShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final SimpleLocation simpleLocation = new SimpleLocation(context);
        simpleLocation.beginUpdates();
        if (Prefs.with(context).readBoolean(Constants.PREFERENCES_ALERT_ENABLED)) {
            final String phoneNumber = Prefs.with(context)
                    .read(Constants.PREFERENCES_SELECTED_CONTACT_NUMBER);
            final String smsMessage = Prefs.with(context)
                    .read(Constants.PREFERENCES_SMS_MESSAGE) + " " + simpleLocation.getLatitude()
                    + ", " + simpleLocation.getLongitude();
            SmsUtils.sendSMS(context, phoneNumber, smsMessage);
        }
        simpleLocation.endUpdates();
    }
}
