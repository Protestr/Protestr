package es.dmoral.protestr.detention_alert;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.utils.Constants;

public class DetentionAlertActivity extends BaseActivity implements DetentionAlertView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.enable_alert_button) Button button;

    private static final int ALERT_NOTIFICATION_ID = 0x1000;

    private boolean alertEnabled;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_detention_alert);
        alertEnabled = Prefs.with(this).readBoolean(Constants.PREFERENCES_ALERT_ENABLED);

        setNotificationState();
        setButtonState();
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        getWindow().getDecorView().requestFocus();
    }

    @Override
    protected void setListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertEnabled = !alertEnabled;
                Prefs.with(DetentionAlertActivity.this)
                        .writeBoolean(Constants.PREFERENCES_ALERT_ENABLED, alertEnabled);

                setNotificationState();
                setButtonState();
            }
        });
    }

    @Override
    public void showOngoingNotification() {
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.alert_notification_title))
                .setContentText(getString(R.string.alert_notification_body))
                .setSmallIcon(R.drawable.ic_notifications_active_white_48dp)
                .setOngoing(true);
        notificationManager.notify(ALERT_NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void clearNotification() {
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ALERT_NOTIFICATION_ID);
    }

    @Override
    public void setNotificationState() {
        if (alertEnabled)
            showOngoingNotification();
        else
            clearNotification();
    }

    @Override
    public void setButtonState() {
        if (alertEnabled) {
            button.setBackgroundResource(R.drawable.alert_button_background_disabled);
            button.setText(R.string.disable_alert);
        } else {
            button.setBackgroundResource(R.drawable.alert_button_background);
            button.setText(R.string.enable_alert);
        }
    }
}
