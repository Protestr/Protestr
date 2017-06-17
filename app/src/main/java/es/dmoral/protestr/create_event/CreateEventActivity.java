package es.dmoral.protestr.create_event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.mvc.imagepicker.ImagePicker;

import java.util.Calendar;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.utils.FormatUtils;
import es.dmoral.protestr.utils.ImageUtils;
import es.dmoral.protestr.utils.TimeUtils;
import es.dmoral.toasty.Toasty;

public class CreateEventActivity extends BaseActivity implements CreateEventView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image_placeholder) ImageView imagePlaceholder;
    @BindView(R.id.event_image) ImageView eventImage;
    @BindView(R.id.image_cardview) CardView imageCardView;
    @BindView(R.id.et_event_name) EditText etEventName;
    @BindView(R.id.et_event_description) EditText etEventDescription;
    @BindView(R.id.tv_date) TextView tvDate;
    @BindView(R.id.tv_time) TextView tvTime;

    private Bitmap eventBitmap;
    private Calendar calendar = Calendar.getInstance();

    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minutes = calendar.get(Calendar.MINUTE);

    private static final String EVENT_BITMAP_SAVED_STATE = "EVENT_BITMAP_SAVED_STATE";
    private static final String EVENT_NAME_SAVED_STATE = "EVENT_NAME_SAVED_STATE";
    private static final String EVENT_DESCRIPTION_SAVED_STATE= "EVENT_DESCRIPTION_SAVED_STATE";
    private static final String YEAR_SAVED_STATE = "YEAR_SAVED_STATE";
    private static final String MONTH_SAVED_SAVED_STATE = "MONTH_SAVED_STATE";
    private static final String DAY_OF_MONTH_SAVED_STATE = "DAY_OF_MONTH_SAVED_STATE";
    private static final String HOUR_SAVED_STATE = "HOUR_SAVED_STATE";
    private static final String MINUTES_SAVED_STATE = "MINUTES_SAVED_STATE";

    private BroadcastReceiver minuteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                updateTimeIfNeeded();
            }
        }
    };

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_create_event);
        restoreStates(savedInstanceState);

        setDate(TimeUtils.getTimeInMillis(year, month, dayOfMonth));
        setTime();

        registerReceiver(minuteReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().requestFocus();

        // https://stackoverflow.com/a/40674771/4208583
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void setListeners() {
        imageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(CreateEventActivity.this, getString(R.string.choose_image));
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog =
                        new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        final long timeInMillis = TimeUtils.getTimeInMillis(year, month, dayOfMonth);
                        CreateEventActivity.this.year = year;
                        CreateEventActivity.this.month = month;
                        CreateEventActivity.this.dayOfMonth = dayOfMonth;
                        setDate(timeInMillis);
                        final int minHour = TimeUtils.getHourOfDayFromMillis(System.currentTimeMillis());
                        final int minMinutes = TimeUtils.getMinuteFromMillis(System.currentTimeMillis());
                        if (timeInMillis == calendar.getTimeInMillis()
                                || (hour < minHour || hour < minHour && minutes < minMinutes)) {
                            hour = TimeUtils.getHourOfDayFromMillis(TimeUtils
                                    .getCurrentTimeInMillisStartingFromMinutes());
                            minutes = TimeUtils.getMinuteFromMillis(TimeUtils
                                    .getCurrentTimeInMillisStartingFromMinutes());
                            setTime();
                        }
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePickerDialog rangeTimePickerDialog =
                        new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        final int minHour = calendar.get(Calendar.HOUR_OF_DAY);
                        final int minMinutes = calendar.get(Calendar.MINUTE);
                        final long timeInMillis = TimeUtils.getTimeInMillis(year, month, dayOfMonth);
                        if (timeInMillis > calendar.getTimeInMillis()
                                || (hour >= minHour || hour >= minHour && minutes >= minMinutes)) {
                            CreateEventActivity.this.hour = hour;
                            CreateEventActivity.this.minutes = minutes;
                            setTime();
                        } else {
                            Toasty.error(CreateEventActivity.this, getString(R.string.hour_not_valid)).show();
                        }
                    }
                }, hour, minutes, true);
                rangeTimePickerDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateEventImage(ImagePicker.getImageFromResult(this, requestCode, resultCode, data));
    }

    @Override
    public void restoreStates(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            updateEventImage((Bitmap) savedInstanceState.getParcelable(EVENT_BITMAP_SAVED_STATE));
            etEventName.setText(savedInstanceState.getString(EVENT_NAME_SAVED_STATE));
            etEventDescription.setText(savedInstanceState.getString(EVENT_DESCRIPTION_SAVED_STATE));
            year = savedInstanceState.getInt(YEAR_SAVED_STATE);
            month = savedInstanceState.getInt(MONTH_SAVED_SAVED_STATE);
            dayOfMonth = savedInstanceState.getInt(DAY_OF_MONTH_SAVED_STATE);
            hour = savedInstanceState.getInt(HOUR_SAVED_STATE);
            minutes = savedInstanceState.getInt(MINUTES_SAVED_STATE);
        }
    }

    @Override
    public void updateEventImage(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            eventBitmap = bitmap;
            imagePlaceholder.setVisibility(View.GONE);
            Glide.with(this)
                    .load(ImageUtils.bitmapToByteArray(eventBitmap))
                    .asBitmap()
                    .into(eventImage);
        }
    }

    @Override
    public void updateTimeIfNeeded() {
        final long timeInMillis = TimeUtils.getTimeInMillis(year, month, dayOfMonth, hour, minutes);
        final long currentTimeInMillis = TimeUtils.getCurrentTimeInMillisStartingFromMinutes();
        if (timeInMillis < currentTimeInMillis) {
            final Calendar auxCalendar = Calendar.getInstance();
            year = auxCalendar.get(Calendar.YEAR);
            month = auxCalendar.get(Calendar.MONTH);
            dayOfMonth = auxCalendar.get(Calendar.DAY_OF_MONTH);
            hour = auxCalendar.get(Calendar.HOUR_OF_DAY);
            minutes = auxCalendar.get(Calendar.MINUTE);
            setDate(timeInMillis);
            setTime();
        }
    }

    @Override
    public void setDate(long timeInMillis) {
        tvDate.setText(getString(R.string.date,
                FormatUtils.formatDateByDefaultLocale(timeInMillis)));
    }

    @Override
    public void setTime() {
        tvTime.setText(getString(R.string.time,
                FormatUtils.addLeadingZero(hour),
                FormatUtils.addLeadingZero(minutes)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_event:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EVENT_BITMAP_SAVED_STATE, eventBitmap);
        outState.putString(EVENT_NAME_SAVED_STATE, etEventName.getText().toString().trim());
        outState.putString(EVENT_DESCRIPTION_SAVED_STATE, etEventDescription.getText().toString().trim());
        outState.putInt(YEAR_SAVED_STATE, year);
        outState.putInt(MONTH_SAVED_SAVED_STATE, month);
        outState.putInt(DAY_OF_MONTH_SAVED_STATE, dayOfMonth);
        outState.putInt(HOUR_SAVED_STATE, hour);
        outState.putInt(MINUTES_SAVED_STATE, minutes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(minuteReceiver);
        eventBitmap = null;
    }
}
