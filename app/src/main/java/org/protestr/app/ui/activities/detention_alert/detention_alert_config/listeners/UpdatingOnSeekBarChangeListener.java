package org.protestr.app.ui.activities.detention_alert.detention_alert_config.listeners;

import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by someone on 19/06/17.
 */

public abstract class UpdatingOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private TextView tvToUpdate;
    private int minValue;
    private int multiplier;

    public UpdatingOnSeekBarChangeListener() {
        // empty;
    }

    public UpdatingOnSeekBarChangeListener(@Nullable TextView tvToUpdate, int minValue) {
        this.tvToUpdate = tvToUpdate;
        this.minValue = minValue;
    }

    public UpdatingOnSeekBarChangeListener(@Nullable TextView tvToUpdate, int minValue,
                                           int multiplier) {
        this(tvToUpdate, minValue);
        this.multiplier = multiplier;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int finalProgress = progress;
        if (multiplier > 0) {
            finalProgress *= multiplier;
        }
        onValueChanged(finalProgress + minValue);
        if (tvToUpdate != null)
            tvToUpdate.setText(String.valueOf(finalProgress + minValue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // unused
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // unused
    }

    public abstract void onValueChanged(int newValue);
}
