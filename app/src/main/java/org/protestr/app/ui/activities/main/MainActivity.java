package org.protestr.app.ui.activities.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.MapView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.protestr.app.data.fcm.FCMHelper;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.create_event.CreateEventActivity;
import org.protestr.app.ui.activities.detention_alert.DetentionAlertActivity;
import org.protestr.app.ui.activities.login.LoginActivity;
import org.protestr.app.ui.activities.scan_event_qr.ScanEventQrActivity;
import org.protestr.app.ui.activities.settings.SettingsActivity;
import org.protestr.app.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;
import org.protestr.app.R;
import org.protestr.app.data.fcm.FCMHelper;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.BaseActivity;
import org.protestr.app.ui.activities.create_event.CreateEventActivity;
import org.protestr.app.ui.activities.detention_alert.DetentionAlertActivity;
import org.protestr.app.ui.activities.login.LoginActivity;
import org.protestr.app.ui.activities.scan_event_qr.ScanEventQrActivity;
import org.protestr.app.ui.activities.settings.SettingsActivity;
import org.protestr.app.ui.adapters.SubscribedEventAdapter;
import org.protestr.app.ui.fragments.events.EventsFragment;
import org.protestr.app.ui.fragments.subscribed_events.SubscribedEventsFragment;
import org.protestr.app.utils.PreferencesUtils;
import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(org.protestr.app.R.id.toolbar)
    Toolbar toolbar;
    @BindView(org.protestr.app.R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(org.protestr.app.R.id.nav_view)
    NavigationView navigationView;
    @BindView(org.protestr.app.R.id.fab)
    FloatingActionButton fab;
    private LinearLayout headerView;

    private static final int LOCATION_REQUEST_CODE = 226;
    private static final String LOCATION_DIALOG_SHOWING = "LOCATION_DIALOG_SHOWING";
    private static final String CURRENT_LOADED_FRAGMENT = "CURRENT_LOADED_FRAGMENT";
    private boolean isLocationSettingRequestDialogShowing;

    private int lastSelectedMenuItemId;

    private Intent pendingIntent;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LOCATION_DIALOG_SHOWING))
                isLocationSettingRequestDialogShowing = savedInstanceState.getBoolean(LOCATION_DIALOG_SHOWING);
            if (savedInstanceState.containsKey(CURRENT_LOADED_FRAGMENT))
                lastSelectedMenuItemId = savedInstanceState.getInt(CURRENT_LOADED_FRAGMENT);
        }

        super.onCreate(savedInstanceState, org.protestr.app.R.layout.activity_main);
        FCMHelper.subscribeToFCMTopic(this, FCMHelper.ENTIRE_APP_TOPIC);
        requestLocationPermissions();
    }

    private void requestLocationPermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        enableLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toasty.warning(MainActivity.this, getString(org.protestr.app.R.string.location_permission_canceled_warning)).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void setupViews() {
        preLoadGoogleMaps();
        setSupportActionBar(toolbar);
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, org.protestr.app.R.string.navigation_drawer_open, org.protestr.app.R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the hamburger to arrow animation
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (pendingIntent != null) {
                    startActivity(pendingIntent);
                    overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
                    pendingIntent = null;
                }
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        headerView = (LinearLayout) navigationView.getHeaderView(0);
        headerView.findViewById(org.protestr.app.R.id.navigation_header_container).setBackgroundResource(org.protestr.app.R.drawable.nav_header_background);
        final User user = PreferencesUtils.getLoggedUser(this);
        Glide.with(this)
                .load(user.getProfilePicUrl())
                .into((ImageView) headerView.findViewById(org.protestr.app.R.id.nav_image));
        ((TextView) headerView.findViewById(org.protestr.app.R.id.nav_title)).setText(user.getUsername());
        ((TextView) headerView.findViewById(org.protestr.app.R.id.nav_subtitle)).setText(user.getEmail());

        if (lastSelectedMenuItemId == 0)
            lastSelectedMenuItemId = org.protestr.app.R.id.nav_events;
        switch (lastSelectedMenuItemId) {
            case org.protestr.app.R.id.nav_events:
                setTitle(org.protestr.app.R.string.new_events);
                navigationView.getMenu().findItem(org.protestr.app.R.id.nav_events).setChecked(true);
                addFragment(EventsFragment.newInstance());
                fab.show();
                break;
            case org.protestr.app.R.id.nav_subscribed_events:
                setTitle(org.protestr.app.R.string.subscribed_events);
                navigationView.getMenu().findItem(org.protestr.app.R.id.nav_subscribed_events).setChecked(true);
                addFragment(SubscribedEventsFragment.newInstance());
                fab.hide();
                break;
        }
    }

    @Override
    protected void setListeners() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(org.protestr.app.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case org.protestr.app.R.id.action_logout:
                Prefs.with(this).clear();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
                finish();
                return true;
            case org.protestr.app.R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (lastSelectedMenuItemId == item.getItemId()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        boolean isFragment;

        switch (item.getItemId()) {
            case org.protestr.app.R.id.nav_events:
                setTitle(item.getTitle());
                addFragment(EventsFragment.newInstance());
                fab.show();
                isFragment = true;
                break;
            case org.protestr.app.R.id.nav_subscribed_events:
                setTitle(item.getTitle());
                addFragment(SubscribedEventsFragment.newInstance());
                fab.hide();
                isFragment = true;
                break;
            case org.protestr.app.R.id.nav_panic_alert:
                pendingIntent = new Intent(this, DetentionAlertActivity.class);
                isFragment = false;
                break;
            case org.protestr.app.R.id.nav_qr_scan:
                pendingIntent = new Intent(this, ScanEventQrActivity.class);
                isFragment = false;
                break;
            default:
                isFragment = false;
        }

        if (isFragment) {
            lastSelectedMenuItemId = item.getItemId();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addFragment(@NonNull Fragment newFragment) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(org.protestr.app.R.id.content_main, newFragment);
        fragmentTransaction.commitNow();
    }

    @Override
    public void showFab() {
        fab.show();
    }

    @Override
    public void hideFab() {
        if (fab.isShown())
            fab.hide();
    }

    @OnClick(org.protestr.app.R.id.fab)
    @Override
    public void createEventAction() {
        startActivity(new Intent(MainActivity.this, CreateEventActivity.class));
        overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
    }

    @Override
    public void enableLocation() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        LocationRequest locationRequestHighAccuracy = LocationRequest.create();
        locationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequest locationRequestBalancedPowerAccuracy = LocationRequest.create();
        locationRequestBalancedPowerAccuracy.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequestHighAccuracy)
                .addLocationRequest(locationRequestBalancedPowerAccuracy);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            if (!isLocationSettingRequestDialogShowing) {
                                status.startResolutionForResult(MainActivity.this, LOCATION_REQUEST_CODE);
                                isLocationSettingRequestDialogShowing = true;
                            }
                        } catch (Exception ignored) {
                            isLocationSettingRequestDialogShowing = false;
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    // As seen in http://stackoverflow.com/a/29246677
    @Override
    public void preLoadGoogleMaps() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                } catch (Exception ignored) {
                    // ignored
                }
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(LOCATION_DIALOG_SHOWING, isLocationSettingRequestDialogShowing);
        outState.putInt(CURRENT_LOADED_FRAGMENT, lastSelectedMenuItemId);
    }
}
