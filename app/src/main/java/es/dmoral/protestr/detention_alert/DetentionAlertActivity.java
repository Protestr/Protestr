package es.dmoral.protestr.detention_alert;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.toasty.Toasty;

public class DetentionAlertActivity extends BaseActivity implements DetentionAlertView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.enable_alert_button) Button enableAlertButton;
    @BindView(R.id.select_contact_bt) Button selectContactButton;
    @BindView(R.id.contact_name) TextView tvContactName;

    private static final int ALERT_NOTIFICATION_ID = 0x1000;
    private static final int PICK_CONTACT = 0x0001;

    private boolean alertEnabled;
    private DetentionAlertPresenter detentionAlertPresenter;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_detention_alert);
        detentionAlertPresenter = new DetentionAlertPresenterImpl(this);
        alertEnabled = Prefs.with(this).readBoolean(Constants.PREFERENCES_ALERT_ENABLED);

        setNotificationState();
        setButtonState();
        setContactName();
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
            }
        });
        selectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
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
            enableAlertButton.setBackgroundResource(R.drawable.alert_button_background);
            enableAlertButton.setText(R.string.enable_alert);
        }
    }

    @Override
    public void setContactName() {
        final String displayName = Prefs.with(this).read(Constants.PREFERENCES_SELECTED_CONTACT_NAME);
        if (!displayName.isEmpty())
            tvContactName.setText(displayName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detentionAlertPresenter.onDestroy();
    }
}
