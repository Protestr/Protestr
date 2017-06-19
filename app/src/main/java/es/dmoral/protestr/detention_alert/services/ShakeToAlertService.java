package es.dmoral.protestr.detention_alert.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.IntDef;
import android.util.Log;

import com.squareup.seismic.ShakeDetector;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.detention_alert.DetentionAlertActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.SmsUtils;
import im.delight.android.location.SimpleLocation;

import static es.dmoral.protestr.detention_alert.DetentionAlertActivity.ALERT_NOTIFICATION_ID;

public class ShakeToAlertService extends Service implements ShakeDetector.Listener {

    private ShakeDetector shakeDetector;
    private boolean active = true;
    private Vibrator vibrator;
    private int shakeCount = 0;

    private long lastShake = 0;
    private int shakeCountThreshold;
    private int shakeResetThreshold;
    private int sensorSensitivity;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shakeCountThreshold = Prefs.with(this).readInt(Constants.PREFERENCES_SHAKE_NUMBER, 6);
        sensorSensitivity = Prefs.with(this).readInt(Constants.PREFERENCES_SENSOR_SENSITIVITY,
                ShakeDetector.SENSITIVITY_LIGHT);
        shakeResetThreshold = Prefs.with(this).readInt(Constants.PREFERENCES_TIME_TO_RESTART, 500);

        listenForShakes();
        return START_STICKY_COMPATIBILITY;
    }

    private void listenForShakes() {
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        shakeDetector = new ShakeDetector(this);
        shakeDetector.setSensitivity(sensorSensitivity);
        shakeDetector.start(sensorManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shakeDetector != null)
            shakeDetector.stop();
    }

    @Override
    public void hearShake() {
        if (shakeCount == shakeCountThreshold && active) {
            vibrator.vibrate(500);
            updateState();
            clearNotification();
            //requestSmsSend();
            sendBroadcast(new Intent(Constants.BROADCAST_SMS_SENT));
            shakeCount = 0;
            active = false;
            stopSelf();
        } else {
            if (lastShake > System.currentTimeMillis() + shakeResetThreshold) {
                shakeCount = 1;
            } else {
                shakeCount++;
            }
        }
        lastShake = System.currentTimeMillis();
    }

    private void requestSmsSend() {
        final SimpleLocation simpleLocation = new SimpleLocation(getApplicationContext());
        simpleLocation.beginUpdates();
        if (Prefs.with(getApplicationContext()).readBoolean(Constants.PREFERENCES_ALERT_ENABLED)) {
            final String phoneNumber = Prefs.with(getApplicationContext())
                    .read(Constants.PREFERENCES_SELECTED_CONTACT_NUMBER);
            final String smsMessage = Prefs.with(getApplicationContext())
                    .read(Constants.PREFERENCES_SMS_MESSAGE) + " " + simpleLocation.getLatitude()
                    + ", " + simpleLocation.getLongitude();
            SmsUtils.sendSMS(getApplicationContext(), phoneNumber, smsMessage);
        }
        simpleLocation.endUpdates();
    }

    private void updateState() {
        Prefs.with(this).writeBoolean(Constants.PREFERENCES_ALERT_ENABLED, false);
    }

    private void clearNotification() {
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ALERT_NOTIFICATION_ID);
    }
}
