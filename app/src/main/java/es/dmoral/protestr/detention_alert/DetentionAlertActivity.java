package es.dmoral.protestr.detention_alert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.detention_alert.services.ShakeToAlertService;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.toasty.Toasty;
import im.delight.android.location.SimpleLocation;

public class DetentionAlertActivity extends BaseActivity implements DetentionAlertView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.enable_alert_button) Button enableAlertButton;
    @BindView(R.id.select_contact_bt) Button selectContactButton;
    @BindView(R.id.contact_name) TextView tvContactName;
    @BindView(R.id.detention_alert_message) EditText detentionAlertMessage;

    private static final int PICK_CONTACT = 0x0001;
    private static final int ALERT_NOTIFICATION_ID = 0x1000;
    private static final int LOCATION_PERMISSION = 0x2000;

    private boolean alertEnabled;
    private DetentionAlertPresenter detentionAlertPresenter;

    private BroadcastReceiver broadcastReceiver;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_detention_alert);
        detentionAlertPresenter = new DetentionAlertPresenterImpl(this);
        alertEnabled = Prefs.with(this).readBoolean(Constants.PREFERENCES_ALERT_ENABLED);

        requestLocationPermissions();
        showLocationRequestDialog();

        registerReceiver();
        setNotificationState();
        setButtonState();
        setMessage();
        setContactName();
        setServiceState();
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().requestFocus();
    }

    @Override
    protected void setListeners() {
        enableAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertEnabled = !alertEnabled;
                Prefs.with(DetentionAlertActivity.this)
                        .writeBoolean(Constants.PREFERENCES_ALERT_ENABLED, alertEnabled);

                setNotificationState();
                setButtonState();
                setServiceState();
            }
        });
        selectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });
    }

    private void registerReceiver() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_SMS_SENT);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_SMS_SENT)) {
                    alertEnabled = false;
                    Prefs.with(DetentionAlertActivity.this)
                            .writeBoolean(Constants.PREFERENCES_ALERT_ENABLED, alertEnabled);

                    setNotificationState();
                    setButtonState();

                    //TODO for testing
                    Toasty.info(DetentionAlertActivity.this, "Alert sms has been sent").show();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setServiceState() {
        if (alertEnabled) {
            startService(new Intent(this, ShakeToAlertService.class));
        } else {
            stopService(new Intent(this, ShakeToAlertService.class));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_CONTACT) {
                detentionAlertPresenter.requestContactInfo(data.getData());
            }
        }
    }

    @Override
    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void contactSelected(String displayName) {
        tvContactName.setText(displayName);
        Toasty.success(this, getString(R.string.contact_selected)).show();
    }

    @Override
    public void contactError() {
        Toasty.error(this, getString(R.string.error_selecting_contact)).show();
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
            enableAlertButton.setBackgroundResource(R.drawable.alert_button_background_disabled);
            enableAlertButton.setText(R.string.disable_alert);
        } else {
            final String smsMessage = detentionAlertMessage.getText().toString().trim();
            if (!smsMessage.isEmpty())
                Prefs.with(DetentionAlertActivity.this).write(Constants.PREFERENCES_SMS_MESSAGE,
                        detentionAlertMessage.getText().toString().trim());
            enableAlertButton.setBackgroundResource(R.drawable.alert_button_background);
            enableAlertButton.setText(R.string.enable_alert);
        }
    }

    @Override
    public void setMessage() {
        final String smsMessage = Prefs.with(this).read(Constants.PREFERENCES_SMS_MESSAGE);
        if (!smsMessage.isEmpty())
            detentionAlertMessage.setText(smsMessage);
    }

    @Override
    public void setContactName() {
        final String displayName = Prefs.with(this).read(Constants.PREFERENCES_SELECTED_CONTACT_NAME);
        if (!displayName.isEmpty())
            tvContactName.setText(displayName);
    }

    @Override
    public void showLocationRequestDialog() {
        final SimpleLocation simpleLocation = new SimpleLocation(DetentionAlertActivity.this);
        if (!simpleLocation.hasLocationEnabled()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.location_dialog_title)
                    .content(R.string.location_dialog_msg)
                    .positiveText(android.R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SimpleLocation.openSettings(DetentionAlertActivity.this);
                        }
                    })
                    .negativeText(android.R.string.cancel)
                    .show();
        }
    }

    private boolean requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detentionAlertPresenter.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }
}
