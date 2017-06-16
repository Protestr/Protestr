package es.dmoral.protestr.create_event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import es.dmoral.toasty.Toasty;

public class CreateEventActivity extends BaseActivity implements CreateEventView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image_placeholder) ImageView imagePlaceholder;
    @BindView(R.id.event_image) ImageView eventImage;
    @BindView(R.id.image_cardview) CardView imageCardView;
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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_create_event);
        if (savedInstanceState != null) {
            updateEventImage((Bitmap) savedInstanceState.getParcelable(EVENT_BITMAP_SAVED_STATE));
        }
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

        tvDate.setText(getString(R.string.date,
                FormatUtils.formatDateByDefaultLocale(calendar.getTimeInMillis())));
        tvTime.setText(getString(R.string.time,
                FormatUtils.addLeadingZero(hour),
                FormatUtils.addLeadingZero(minutes)));

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
                        final Calendar auxCalendar = Calendar.getInstance();
                        auxCalendar.set(Calendar.YEAR, year);
                        auxCalendar.set(Calendar.MONTH, month);
                        auxCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        auxCalendar.clear(Calendar.HOUR_OF_DAY);
                        auxCalendar.clear(Calendar.MINUTE);
                        auxCalendar.clear(Calendar.SECOND);
                        auxCalendar.clear(Calendar.MILLISECOND);
                        CreateEventActivity.this.year = year;
                        CreateEventActivity.this.month = month;
                        CreateEventActivity.this.dayOfMonth = dayOfMonth;
                        tvDate.setText(getString(R.string.date,
                                FormatUtils.formatDateByDefaultLocale(auxCalendar.getTimeInMillis())));
                        auxCalendar.setTimeInMillis(System.currentTimeMillis());
                        final int minHour = auxCalendar.get(Calendar.HOUR_OF_DAY);
                        final int minMinutes = auxCalendar.get(Calendar.MINUTE);
                        if (auxCalendar.getTimeInMillis() == calendar.getTimeInMillis()
                                || (hour < minHour || hour < minHour && minutes < minMinutes)) {
                            hour = auxCalendar.get(Calendar.HOUR_OF_DAY);
                            minutes = auxCalendar.get(Calendar.MINUTE);
                            tvTime.setText(getString(R.string.time,
                                    FormatUtils.addLeadingZero(hour),
                                    FormatUtils.addLeadingZero(minutes)));
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
                        final Calendar auxCalendar = Calendar.getInstance();
                        auxCalendar.set(Calendar.YEAR, year);
                        auxCalendar.set(Calendar.MONTH, month);
                        auxCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        auxCalendar.clear(Calendar.HOUR_OF_DAY);
                        auxCalendar.clear(Calendar.MINUTE);
                        auxCalendar.clear(Calendar.SECOND);
                        auxCalendar.clear(Calendar.MILLISECOND);
                        if (auxCalendar.getTimeInMillis() > calendar.getTimeInMillis()
                                || (hour >= minHour || hour >= minHour && minutes >= minMinutes)) {
                            CreateEventActivity.this.hour = hour;
                            CreateEventActivity.this.minutes = minutes;
                            tvTime.setText(getString(R.string.time,
                                    FormatUtils.addLeadingZero(hour),
                                    FormatUtils.addLeadingZero(minutes)));
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBitmap = null;
    }
}
