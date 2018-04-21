package org.protestr.app.ui.activities.create_event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mvc.imagepicker.ImagePicker;

import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.UpdatePool;
import org.protestr.app.ui.activities.create_event.add_admins.AddAdminsActivity;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.FormatUtils;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.LocationUtils;
import org.protestr.app.utils.PreferencesUtils;
import org.protestr.app.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.UpdatePool;
import org.protestr.app.ui.activities.BaseActivity;
import org.protestr.app.ui.activities.create_event.add_admins.AddAdminsActivity;
import org.protestr.app.ui.custom.ScrollFriendlyMapView;
import org.protestr.app.ui.fragments.events.EventsFragment;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.FormatUtils;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.LocationUtils;
import org.protestr.app.utils.PreferencesUtils;
import org.protestr.app.utils.TimeUtils;
import es.dmoral.toasty.Toasty;

public class CreateEventActivity extends BaseActivity implements CreateEventView,
        OnMapReadyCallback {

    @BindView(org.protestr.app.R.id.toolbar)
    Toolbar toolbar;
    @BindView(org.protestr.app.R.id.image_placeholder)
    ImageView imagePlaceholder;
    @BindView(org.protestr.app.R.id.event_image)
    ImageView eventImage;
    @BindView(org.protestr.app.R.id.image_cardview)
    CardView imageCardView;
    @BindView(org.protestr.app.R.id.et_event_name)
    EditText etEventName;
    @BindView(org.protestr.app.R.id.et_event_description)
    EditText etEventDescription;
    @BindView(org.protestr.app.R.id.tv_admins)
    TextView tvAdmins;
    @BindView(org.protestr.app.R.id.et_event_location)
    EditText etEventLocation;
    @BindView(org.protestr.app.R.id.tv_date)
    TextView tvDate;
    @BindView(org.protestr.app.R.id.tv_time)
    TextView tvTime;
    @BindView(org.protestr.app.R.id.map_view)
    ScrollFriendlyMapView mapView;

    private MaterialDialog progressDialog;

    private Intent eventBitmapIntent;
    private boolean isImageSelected;
    private Calendar calendar = Calendar.getInstance();
    private GoogleMap googleMap;
    private CreateEventPresenter createEventPresenter;

    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minutes = calendar.get(Calendar.MINUTE);

    private double latitude = 0;
    private double longitude = 0;
    private String iso3 = "";

    public static final int ADDED_ADMINS_CODE = 1;

    private static final String EVENT_BITMAP_SAVED_STATE = "EVENT_BITMAP_SAVED_STATE";
    private static final String YEAR_SAVED_STATE = "YEAR_SAVED_STATE";
    private static final String MONTH_SAVED_SAVED_STATE = "MONTH_SAVED_STATE";
    private static final String DAY_OF_MONTH_SAVED_STATE = "DAY_OF_MONTH_SAVED_STATE";
    private static final String HOUR_SAVED_STATE = "HOUR_SAVED_STATE";
    private static final String MINUTES_SAVED_STATE = "MINUTES_SAVED_STATE";
    private static final String EVENT_LATITUDE_SAVED_STATE = "EVENT_LATITUDE_SAVED_STATE";
    private static final String EVENT_LONGITUDE_SAVED_STATE = "EVENT_LONGITUDE_SAVED_STATE";
    private static final String EVENT_ISO3_CODE_SAVED_STATE = "EVENT_ISO3_CODE_SAVED_STATE";
    private static final String MAP_VIEW_SAVED_STATE = "MAP_VIEW_SAVED_STATE";
    private static final String ADDED_USERS_SAVED_STATE = "ADDED_USERS_SAVED_STATE";

    private static final long TEXT_DELAY = 500;
    private static final long TEXT_DELAY_THRESHOLD = 250;

    private ArrayList<User> addedUsers;

    private long lastTypeTimestamp = 0;
    private String lastTypedMessage = "";
    private Handler typeHandler = new Handler();

    private Runnable typeRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > lastTypeTimestamp + TEXT_DELAY_THRESHOLD) {
                toolbar.getMenu().findItem(org.protestr.app.R.id.action_create_event).setEnabled(false);
                toolbar.getMenu().findItem(org.protestr.app.R.id.action_create_event).getIcon().setAlpha(128);
                LocationUtils.getLocationFromAddress(CreateEventActivity.this,
                        lastTypedMessage, new LocationUtils.OnAddressDecodedListener() {
                            @Override
                            public void onAddressDecoded(final LatLng latLng, String iso3) {
                                CreateEventActivity.this.iso3 = iso3;
                                latitude = latLng.latitude;
                                longitude = latLng.longitude;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toolbar.getMenu().findItem(org.protestr.app.R.id.action_create_event).setEnabled(true);
                                        toolbar.getMenu().findItem(org.protestr.app.R.id.action_create_event).getIcon().setAlpha(255);
                                        moveMapCamera(latLng);
                                    }
                                });
                            }
                        });
            }
        }
    };

    private BroadcastReceiver minuteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                updateTimeIfNeeded();
            }
        }
    };

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, org.protestr.app.R.layout.activity_create_event);
        restoreStates(savedInstanceState);
        createEventPresenter = new CreateEventPresenterImpl(this);

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

        mapView.getMapAsync(this);

        // https://stackoverflow.com/a/40674771/4208583
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void setListeners() {
        imageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ImagePicker.pickImage(CreateEventActivity.this, getString(org.protestr.app.R.string.choose_image));
                } catch (NullPointerException npe) {
                    Log.e(CreateEventActivity.this.toString(), "No suitable app found to choose an image.");
                }
            }
        });
        tvAdmins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent toAddAdmins = new Intent(CreateEventActivity.this, AddAdminsActivity.class);
                toAddAdmins.putParcelableArrayListExtra(Constants.ADDED_ADMINS_EXTRA, addedUsers);
                startActivityForResult(toAddAdmins, ADDED_ADMINS_CODE);
                overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
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
                                    Toasty.error(CreateEventActivity.this, getString(org.protestr.app.R.string.hour_not_valid)).show();
                                }
                            }
                        }, hour, minutes, true);
                rangeTimePickerDialog.show();
            }
        });
        etEventLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                // unused
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                typeHandler.removeCallbacks(typeRunnable);
            }

            @Override
            public void afterTextChanged(Editable e) {
                if (googleMap != null) {
                    lastTypeTimestamp = System.currentTimeMillis();
                    lastTypedMessage = e.toString();
                    typeHandler.postDelayed(typeRunnable, TEXT_DELAY);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        moveMapCamera(new LatLng(latitude, longitude));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDED_ADMINS_CODE) {
                addedUsers = data.getParcelableArrayListExtra(Constants.ADDED_ADMINS_EXTRA);
                tvAdmins.setText(getString(org.protestr.app.R.string.add_admins_with_number, addedUsers.size()));
            } else if (requestCode == ImagePicker.PICK_IMAGE_REQUEST_CODE) {
                eventBitmapIntent = data;
                updateEventImage(ImagePicker.getImageFromResult(this, requestCode, resultCode, data));
            }
        }
    }

    @Override
    public void restoreStates(@Nullable Bundle savedInstanceState) {
        Bundle mapViewSavedInstanceState = null;
        if (savedInstanceState != null) {
            eventBitmapIntent = savedInstanceState.getParcelable(EVENT_BITMAP_SAVED_STATE);
            updateEventImage(ImagePicker.getImageFromResult(this,  ImagePicker.PICK_IMAGE_REQUEST_CODE, RESULT_OK, eventBitmapIntent));
            year = savedInstanceState.getInt(YEAR_SAVED_STATE);
            month = savedInstanceState.getInt(MONTH_SAVED_SAVED_STATE);
            dayOfMonth = savedInstanceState.getInt(DAY_OF_MONTH_SAVED_STATE);
            hour = savedInstanceState.getInt(HOUR_SAVED_STATE);
            minutes = savedInstanceState.getInt(MINUTES_SAVED_STATE);
            latitude = savedInstanceState.getDouble(EVENT_LATITUDE_SAVED_STATE);
            longitude = savedInstanceState.getDouble(EVENT_LONGITUDE_SAVED_STATE);
            iso3 = savedInstanceState.getString(EVENT_ISO3_CODE_SAVED_STATE);
            addedUsers = savedInstanceState.getParcelableArrayList(ADDED_USERS_SAVED_STATE);
            mapViewSavedInstanceState = savedInstanceState.getBundle(MAP_VIEW_SAVED_STATE);
        } else {
            addedUsers = new ArrayList<>();
            addedUsers.add(PreferencesUtils.getLoggedUser(this));
        }

        if (mapView != null)
            mapView.onCreate(mapViewSavedInstanceState);

        tvAdmins.setText(getString(org.protestr.app.R.string.add_admins_with_number, addedUsers.size()));
    }

    @Override
    public void updateEventImage(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            isImageSelected = true;
            imagePlaceholder.setVisibility(View.GONE);
            eventImage.setImageBitmap(bitmap);
        } else {
            isImageSelected = false;
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
            setDate(currentTimeInMillis);
            setTime();
        }
    }

    @Override
    public boolean checkIfCanSubmit() {
        return latitude != 0 && longitude != 0 && !etEventName.getText().toString().trim().isEmpty()
                && !etEventDescription.getText().toString().isEmpty() && isImageSelected;
    }

    @Override
    public void moveMapCamera(final LatLng latLng) {
        if (latLng.latitude == 0 && latLng.longitude == 0)
            return;
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(12f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng));
                    }

                    @Override
                    public void onCancel() {
                        // ignored
                    }
                });
    }

    @Override
    public void setDate(long timeInMillis) {
        tvDate.setText(getString(org.protestr.app.R.string.date,
                FormatUtils.formatDateByDefaultLocale(timeInMillis)));
    }

    @Override
    public void setTime() {
        tvTime.setText(getString(org.protestr.app.R.string.time,
                FormatUtils.addLeadingZero(hour),
                FormatUtils.addLeadingZero(minutes)));
    }

    @Override
    public void onEventCreated() {
        UpdatePool.needsUpdates(EventsFragment.class.toString());
        Toasty.success(this, getString(org.protestr.app.R.string.event_created)).show();
        onBackPressed();
    }

    @Override
    public void showProgress() {
        progressDialog = new MaterialDialog.Builder(this)
                .content(org.protestr.app.R.string.creating_event)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showError(String message) {
        Toasty.error(this, message).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(org.protestr.app.R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case org.protestr.app.R.id.action_create_event:
                KeyboardUtils.closeKeyboard(getCurrentFocus());
                if (checkIfCanSubmit()) {
                    showProgress();
                    long a = TimeUtils.getTimeInMillis(year, month, dayOfMonth, hour, minutes);
                    createEventPresenter.createEvent(ImagePicker.getImageFromResult(this,
                            ImagePicker.PICK_IMAGE_REQUEST_CODE, RESULT_OK, eventBitmapIntent),
                            etEventName.getText().toString().trim(), etEventDescription.getText().toString().trim(),
                            TimeUtils.getTimeInMillis(year, month, dayOfMonth, hour, minutes),
                            etEventLocation.getText().toString().trim(), latitude, longitude, iso3,
                            addedUsers);
                } else {
                    Toasty.error(CreateEventActivity.this, getString(org.protestr.app.R.string.fill_all_fields_error)).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // As seen in http://stackoverflow.com/a/39525123/4208583
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //This MUST be done before saving any of your own or your base class's variables
        final Bundle mapViewSaveState = new Bundle(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle(MAP_VIEW_SAVED_STATE, mapViewSaveState);

        super.onSaveInstanceState(outState);
        outState.putParcelable(EVENT_BITMAP_SAVED_STATE, eventBitmapIntent);
        outState.putParcelableArrayList(ADDED_USERS_SAVED_STATE, addedUsers);
        outState.putInt(YEAR_SAVED_STATE, year);
        outState.putInt(MONTH_SAVED_SAVED_STATE, month);
        outState.putInt(DAY_OF_MONTH_SAVED_STATE, dayOfMonth);
        outState.putInt(HOUR_SAVED_STATE, hour);
        outState.putInt(MINUTES_SAVED_STATE, minutes);
        outState.putDouble(EVENT_LATITUDE_SAVED_STATE, latitude);
        outState.putDouble(EVENT_LONGITUDE_SAVED_STATE, longitude);
        outState.putString(EVENT_ISO3_CODE_SAVED_STATE, iso3);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(minuteReceiver);
        eventBitmapIntent = null;
        createEventPresenter.onDestroy();
        if (mapView != null)
            mapView.onDestroy();

    }
}
