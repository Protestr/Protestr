package es.dmoral.protestr.detention_alert.detention_alert_config;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.seismic.ShakeDetector;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.detention_alert.detention_alert_config.listeners.UpdatingOnSeekBarChangeListener;
import es.dmoral.protestr.utils.Constants;

public class DetentionAlertConfigActivity extends BaseActivity implements DetentionAlertConfigView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_shake_number) TextView tvShakeNumber;
    @BindView(R.id.seekbar_shake_number) SeekBar seekbarShakeNumber;
    @BindView(R.id.tv_sensitivity) TextView tvSensitivity;
    @BindView(R.id.seekbar_sensitivity) SeekBar seekbarSensitivity;
    @BindView(R.id.tv_time_until_restart) TextView tvTimeUntilRestart;
    @BindView(R.id.seekbar_time_until_restart) SeekBar seekbarTimeUntilRestart;

    private int shakeNumber;
    private int sensorSensitivity;
    private int timeUntilRestart;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_detention_alert_config);
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
        shakeNumber = Prefs.with(this).readInt(Constants.PREFERENCES_SHAKE_NUMBER, 6);
        sensorSensitivity = Prefs.with(this).readInt(Constants.PREFERENCES_SENSOR_SENSITIVITY,
                ShakeDetector.SENSITIVITY_LIGHT);
        timeUntilRestart = Prefs.with(this).readInt(Constants.PREFERENCES_TIME_TO_RESTART, 500);

        seekbarShakeNumber.setKeyProgressIncrement(1);
        seekbarSensitivity.setKeyProgressIncrement(1);
        seekbarTimeUntilRestart.setKeyProgressIncrement(1);

        seekbarShakeNumber.setProgress(shakeNumber - 3);
        seekbarSensitivity.setProgress(sensorSensitivity - 10);
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
                tvSensitivity, 10) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.configure_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_sensor_config:
                Prefs.with(this).writeInt(Constants.PREFERENCES_SHAKE_NUMBER, shakeNumber);
                Prefs.with(this).writeInt(Constants.PREFERENCES_SENSOR_SENSITIVITY, sensorSensitivity);
                Prefs.with(this).writeInt(Constants.PREFERENCES_TIME_TO_RESTART, timeUntilRestart);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
