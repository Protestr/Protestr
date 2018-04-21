package org.protestr.app.ui.activities.detention_alert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.protestr.app.ui.activities.detention_alert.detention_alert_config.DetentionAlertConfigActivity;
import org.protestr.app.ui.activities.detention_alert.services.ShakeToAlertService;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;
import org.protestr.app.R;
import org.protestr.app.ui.activities.BaseActivity;
import org.protestr.app.ui.activities.detention_alert.detention_alert_config.DetentionAlertConfigActivity;
import org.protestr.app.ui.activities.detention_alert.services.ShakeToAlertService;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.PreferencesUtils;
import es.dmoral.toasty.Toasty;

public class DetentionAlertActivity extends BaseActivity implements DetentionAlertView {

    @BindView(org.protestr.app.R.id.toolbar)
    Toolbar toolbar;
    @BindView(org.protestr.app.R.id.enable_alert_button)
    Button enableAlertButton;
    @BindView(org.protestr.app.R.id.select_contact_bt)
    Button selectContactButton;
    @BindView(org.protestr.app.R.id.contact_name)
    TextView tvContactName;
    @BindView(org.protestr.app.R.id.detention_alert_message)
    EditText detentionAlertMessage;

    private static final int PICK_CONTACT = 0x0001;
    public static final int ALERT_NOTIFICATION_ID = 0x1000;

    private boolean alertEnabled;
    private DetentionAlertPresenter detentionAlertPresenter;

    private BroadcastReceiver broadcastReceiver;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, org.protestr.app.R.layout.activity_detention_alert);
        detentionAlertPresenter = new DetentionAlertPresenterImpl(this);
        alertEnabled = Prefs.with(this).readBoolean(PreferencesUtils.PREFERENCES_ALERT_ENABLED);

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
        // unused
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(org.protestr.app.R.menu.menu_detention_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case org.protestr.app.R.id.action_configure_detection_alert:
                startActivity(new Intent(DetentionAlertActivity.this, DetentionAlertConfigActivity.class));
                overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void registerReceiver() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_SMS_SENT);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_SMS_SENT)) {
                    alertEnabled = false;
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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_CONTACT) {
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.READ_CONTACTS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                detentionAlertPresenter.requestContactInfo(data.getData());
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // ignored
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        }
    }

    @Override
    public void contactSelected(String displayName) {
        tvContactName.setText(displayName);
        Toasty.success(this, getString(org.protestr.app.R.string.contact_selected)).show();
    }

    @Override
    public void contactError() {
        Toasty.error(this, getString(org.protestr.app.R.string.error_selecting_contact)).show();
    }

    @Override
    public void showOngoingNotification() {
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(org.protestr.app.R.string.alert_notification_title))
                .setContentText(getString(org.protestr.app.R.string.alert_notification_body))
                .setSmallIcon(org.protestr.app.R.drawable.ic_notifications_active_white_48dp)
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
        KeyboardUtils.closeKeyboard(getCurrentFocus());
        final String smsMessage = detentionAlertMessage.getText().toString().trim();
        if (!smsMessage.isEmpty())
            Prefs.with(DetentionAlertActivity.this).write(PreferencesUtils.PREFERENCES_SMS_MESSAGE,
                    smsMessage);
        if (alertEnabled) {
            enableAlertButton.setBackgroundResource(org.protestr.app.R.drawable.alert_button_background_disabled);
            enableAlertButton.setText(org.protestr.app.R.string.disable_alert);
        } else {
            enableAlertButton.setBackgroundResource(org.protestr.app.R.drawable.alert_button_background);
            enableAlertButton.setText(org.protestr.app.R.string.enable_alert);
        }
    }

    @Override
    public void setMessage() {
        final String smsMessage = Prefs.with(this).read(PreferencesUtils.PREFERENCES_SMS_MESSAGE);
        if (!smsMessage.isEmpty())
            detentionAlertMessage.setText(smsMessage);
    }

    @Override
    public void setContactName() {
        final String displayName = Prefs.with(this).read(PreferencesUtils.PREFERENCES_SELECTED_CONTACT_NAME);
        if (!displayName.isEmpty())
            tvContactName.setText(displayName);
    }

    @OnClick(org.protestr.app.R.id.select_contact_bt)
    @Override
    public void selectContactAction() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @OnClick(org.protestr.app.R.id.enable_alert_button)
    @Override
    public void enableAlertAction() {
        if (Prefs.with(this).read(PreferencesUtils.PREFERENCES_SELECTED_CONTACT_NUMBER).isEmpty()) {
            Toasty.info(this, getString(org.protestr.app.R.string.empty_contact)).show();
        } else {
            alertEnabled = !alertEnabled;
            Prefs.with(DetentionAlertActivity.this)
                    .writeBoolean(PreferencesUtils.PREFERENCES_ALERT_ENABLED, alertEnabled);

            setNotificationState();
            setButtonState();
            setServiceState();
        }
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
