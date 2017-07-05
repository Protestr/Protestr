package es.dmoral.protestr.detention_alert.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.squareup.seismic.ShakeDetector;

import java.io.Serializable;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.detention_alert.DetentionAlertActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.PreferencesUtils;
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
    private boolean testing = false;

    public static final String SHAKE_COUNT_THRESHOLD_EXTRA = "SHAKE_COUNT_THRESHOLD_EXTRA";
    public static final String SHAKE_RESET_THRESHOLD_EXTRA = "SHAKE_RESET_THRESHOLD_EXTRA";
    public static final String SENSOR_SENSITIVITY_EXTRA = "SENSOR_SENSITIVITY_EXTRA";

    public static final String WHEN_EXTRA = "WHEN_EXTRA";
    public static final String COUNT_EXTRA = "COUNT_EXTRA";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(SHAKE_COUNT_THRESHOLD_EXTRA) &&
                intent.hasExtra(SHAKE_RESET_THRESHOLD_EXTRA) &&
                intent.hasExtra(SENSOR_SENSITIVITY_EXTRA)) {
            shakeCountThreshold = intent.getExtras().getInt(SHAKE_COUNT_THRESHOLD_EXTRA);
            sensorSensitivity = intent.getExtras().getInt(SENSOR_SENSITIVITY_EXTRA);
            shakeResetThreshold = intent.getExtras().getInt(SHAKE_RESET_THRESHOLD_EXTRA);
            testing = true;
        } else {
            shakeCountThreshold = Prefs.with(this).readInt(PreferencesUtils.PREFERENCES_SHAKE_NUMBER, 6);
            sensorSensitivity = Prefs.with(this).readInt(PreferencesUtils.PREFERENCES_SENSOR_SENSITIVITY,
                    ShakeDetector.SENSITIVITY_LIGHT);
            shakeResetThreshold = Prefs.with(this).readInt(PreferencesUtils.PREFERENCES_TIME_TO_RESTART, 500);
        }

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
            sendShakeCompletedBroadcast(System.currentTimeMillis());
            vibrator.vibrate(500);
            updateState();
            clearNotification();
            if (!testing) {
                //requestSmsSend();
                sendBroadcast(new Intent(Constants.BROADCAST_SMS_SENT));
            }
            shakeCount = 0;
            active = false;
            stopSelf();
        } else {
            if (System.currentTimeMillis() > lastShake + shakeResetThreshold && shakeCount > 0) {
                shakeCount = 1;
                sendShakeCountRestartedBroadcast(System.currentTimeMillis());
            } else {
                shakeCount++;
                sendShakeDetectedBroadcast(System.currentTimeMillis());
            }
        }
        lastShake = System.currentTimeMillis();
    }

    private void requestSmsSend() {
        final SimpleLocation simpleLocation = new SimpleLocation(getApplicationContext());
        simpleLocation.beginUpdates();
        if (Prefs.with(getApplicationContext()).readBoolean(PreferencesUtils.PREFERENCES_ALERT_ENABLED)) {
            final String phoneNumber = Prefs.with(getApplicationContext())
                    .read(PreferencesUtils.PREFERENCES_SELECTED_CONTACT_NUMBER);
            final String smsMessage = Prefs.with(getApplicationContext())
                    .read(PreferencesUtils.PREFERENCES_SMS_MESSAGE) + " " + simpleLocation.getLatitude()
                    + ", " + simpleLocation.getLongitude();
            SmsUtils.sendSMS(getApplicationContext(), phoneNumber, smsMessage);
        }
        simpleLocation.endUpdates();
    }

    private void updateState() {
        Prefs.with(this).writeBoolean(PreferencesUtils.PREFERENCES_ALERT_ENABLED, false);
    }

    private void clearNotification() {
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ALERT_NOTIFICATION_ID);
    }

    private void sendShakeDetectedBroadcast(long when) {
        final Intent intent = new Intent(Constants.BROADCAST_SHAKE_DETECTED);
        intent.putExtra(WHEN_EXTRA, when);
        intent.putExtra(COUNT_EXTRA, shakeCount);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendShakeCompletedBroadcast(long when) {
        final Intent intent = new Intent(Constants.BROADCAST_SHAKE_COMPLETED);
        intent.putExtra(WHEN_EXTRA, when);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendShakeCountRestartedBroadcast(long when) {
        final Intent intent = new Intent(Constants.BROADCAST_SHAKE_RESTARTED);
        intent.putExtra(WHEN_EXTRA, when);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
