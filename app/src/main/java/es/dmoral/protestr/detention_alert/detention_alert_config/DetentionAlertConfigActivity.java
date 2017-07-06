package es.dmoral.protestr.detention_alert.detention_alert_config;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.seismic.ShakeDetector;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.custom.ScrollFriendlyScrollView;
import es.dmoral.protestr.detention_alert.DetentionAlertActivity;
import es.dmoral.protestr.detention_alert.detention_alert_config.listeners.UpdatingOnSeekBarChangeListener;
import es.dmoral.protestr.detention_alert.services.ShakeToAlertService;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.PreferencesUtils;

public class DetentionAlertConfigActivity extends BaseActivity implements DetentionAlertConfigView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_shake_number) TextView tvShakeNumber;
    @BindView(R.id.seekbar_shake_number) SeekBar seekbarShakeNumber;
    @BindView(R.id.tv_sensitivity) TextView tvSensitivity;
    @BindView(R.id.seekbar_sensitivity) SeekBar seekbarSensitivity;
    @BindView(R.id.tv_time_until_restart) TextView tvTimeUntilRestart;
    @BindView(R.id.seekbar_time_until_restart) SeekBar seekbarTimeUntilRestart;
    @BindView(R.id.tv_sensor_log) TextView tvSensorLog;
    @BindView(R.id.enable_test_sensor_button) Button btEnableTestSensor;
    @BindView(R.id.log_scroll_view) ScrollFriendlyScrollView logScrollView;

    private int shakeNumber;
    private int sensorSensitivity;
    private int timeUntilRestart;
    private boolean testEnabled = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long when = 0;
            if (intent.hasExtra(ShakeToAlertService.WHEN_EXTRA))
                when = intent.getLongExtra(ShakeToAlertService.WHEN_EXTRA, 0);

            switch (intent.getAction()) {
                case Constants.BROADCAST_SHAKE_DETECTED:
                    final int count = intent.getIntExtra(ShakeToAlertService.COUNT_EXTRA, 0);
                    tvSensorLog.append(getString(R.string.shake_detected, logTimeFormatter.format(when), count));
                    break;
                case Constants.BROADCAST_SHAKE_COMPLETED:
                    testEnabled = false;
                    setButtonState();
                    LocalBroadcastManager.getInstance(DetentionAlertConfigActivity.this)
                            .unregisterReceiver(broadcastReceiver);
                    tvSensorLog.append(getString(R.string.shake_completed, logTimeFormatter.format(when)));
                    break;
                case Constants.BROADCAST_SHAKE_RESTARTED:
                    tvSensorLog.append(getString(R.string.shake_restarted, logTimeFormatter.format(when)));
                    break;
            }
            logScrollView.fullScroll(View.FOCUS_DOWN);
        }
    };

    private SimpleDateFormat logTimeFormatter = new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH);
    private IntentFilter intentFilter;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_detention_alert_config);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_SHAKE_COMPLETED);
        intentFilter.addAction(Constants.BROADCAST_SHAKE_DETECTED);
        intentFilter.addAction(Constants.BROADCAST_SHAKE_RESTARTED);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().requestFocus();
        setUpSeekBars();
    }

    @Override
    protected void setListeners() {
        setSeekBarListeners();
    }

    @Override
    public void setUpSeekBars() {
        shakeNumber = Prefs.with(this).readInt(PreferencesUtils.PREFERENCES_SHAKE_NUMBER, 6);
        sensorSensitivity = Prefs.with(this).readInt(PreferencesUtils.PREFERENCES_SENSOR_SENSITIVITY,
                ShakeDetector.SENSITIVITY_LIGHT);
        timeUntilRestart = Prefs.with(this).readInt(PreferencesUtils.PREFERENCES_TIME_TO_RESTART, 500);

        seekbarShakeNumber.setKeyProgressIncrement(1);
        seekbarSensitivity.setKeyProgressIncrement(1);
        seekbarTimeUntilRestart.setKeyProgressIncrement(1);

        seekbarShakeNumber.setProgress(shakeNumber - 3);
        seekbarSensitivity.setProgress(sensorSensitivity - 7);
        seekbarTimeUntilRestart.setProgress((timeUntilRestart - 250 ) / 250);

        tvShakeNumber.setText(String.valueOf(shakeNumber));
        tvSensitivity.setText(String.valueOf(sensorSensitivity));
        tvTimeUntilRestart.setText(String.valueOf(timeUntilRestart));
    }

    @Override
    public void setSeekBarListeners() {
        seekbarShakeNumber.setOnSeekBarChangeListener(new UpdatingOnSeekBarChangeListener(
                tvShakeNumber, 3) {
            @Override
            public void onValueChanged(int newValue) {
                shakeNumber = newValue;
            }
        });
        seekbarSensitivity.setOnSeekBarChangeListener(new UpdatingOnSeekBarChangeListener(
                tvSensitivity, 7) {
            @Override
            public void onValueChanged(int newValue) {
                sensorSensitivity = newValue;
            }
        });
        seekbarTimeUntilRestart.setOnSeekBarChangeListener(new UpdatingOnSeekBarChangeListener(
                tvTimeUntilRestart, 250, 250) {
            @Override
            public void onValueChanged(int newValue) {
                timeUntilRestart = newValue;
            }
        });
    }

    @Override
    public void setButtonState() {
        if (testEnabled) {
            btEnableTestSensor.setBackgroundResource(R.drawable.alert_button_background_disabled);
            btEnableTestSensor.setText(R.string.stop_testing_sensor);
            tvSensorLog.setText("");
        } else {
            btEnableTestSensor.setBackgroundResource(R.drawable.alert_button_background);
            btEnableTestSensor.setText(R.string.test_sensor);
        }
    }

    @OnClick(R.id.enable_test_sensor_button)
    @Override
    public void enableTestSensorAction() {
        if (testEnabled) {
            stopService(new Intent(DetentionAlertConfigActivity.this, ShakeToAlertService.class));
            LocalBroadcastManager.getInstance(DetentionAlertConfigActivity.this)
                    .unregisterReceiver(broadcastReceiver);
            testEnabled = false;
        } else {
            final Intent serviceIntent = new Intent(DetentionAlertConfigActivity.this,
                    ShakeToAlertService.class);
            final Bundle interfaceBundle = new Bundle();
            interfaceBundle.putInt(ShakeToAlertService.SHAKE_COUNT_THRESHOLD_EXTRA, shakeNumber);
            interfaceBundle.putInt(ShakeToAlertService.SHAKE_RESET_THRESHOLD_EXTRA, timeUntilRestart);
            interfaceBundle.putInt(ShakeToAlertService.SENSOR_SENSITIVITY_EXTRA, sensorSensitivity);
            serviceIntent.putExtras(interfaceBundle);
            startService(serviceIntent);
            LocalBroadcastManager.getInstance(DetentionAlertConfigActivity.this)
                    .registerReceiver(broadcastReceiver, intentFilter);
            testEnabled = true;
        }
        setButtonState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.configure_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_sensor_config:
                Prefs.with(this).writeInt(PreferencesUtils.PREFERENCES_SHAKE_NUMBER, shakeNumber);
                Prefs.with(this).writeInt(PreferencesUtils.PREFERENCES_SENSOR_SENSITIVITY, sensorSensitivity);
                Prefs.with(this).writeInt(PreferencesUtils.PREFERENCES_TIME_TO_RESTART, timeUntilRestart);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (testEnabled) {
            stopService(new Intent(this, ShakeToAlertService.class));
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }
}
