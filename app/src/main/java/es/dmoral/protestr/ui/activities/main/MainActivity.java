package es.dmoral.protestr.ui.activities.main;

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

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.activities.create_event.CreateEventActivity;
import es.dmoral.protestr.ui.activities.detention_alert.DetentionAlertActivity;
import es.dmoral.protestr.data.fcm.FCMHelper;
import es.dmoral.protestr.ui.fragments.events.EventsFragment;
import es.dmoral.protestr.ui.fragments.subscribed_events.SubscribedEventsFragment;
import es.dmoral.protestr.ui.activities.login.LoginActivity;
import es.dmoral.protestr.data.models.User;
import es.dmoral.protestr.ui.activities.settings.SettingsActivity;
import es.dmoral.protestr.utils.PreferencesUtils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private LinearLayout headerView;

    private int lastSelectedMenuItemId;

    private Intent pendingIntent;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        FCMHelper.subscribeToFCMTopic(this, FCMHelper.ENTIRE_APP_TOPIC);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the hamburger to arrow animation
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (pendingIntent != null) {
                    startActivity(pendingIntent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    pendingIntent = null;
                }
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        headerView = (LinearLayout) navigationView.getHeaderView(0);
        headerView.findViewById(R.id.navigation_header_container).setBackgroundResource(R.drawable.nav_header_background);
        final User user = PreferencesUtils.getLoggedUser(this);
        Glide.with(this)
                .load(user.getProfilePicUrl())
                .into((ImageView) headerView.findViewById(R.id.nav_image));
        ((TextView) headerView.findViewById(R.id.nav_title)).setText(user.getUsername());
        ((TextView) headerView.findViewById(R.id.nav_subtitle)).setText(user.getEmail());
        setTitle(navigationView.getMenu().findItem(R.id.nav_events).getTitle());
        navigationView.getMenu().findItem(R.id.nav_events).setCheckable(true);
        navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
        lastSelectedMenuItemId = R.id.nav_events;
        addFragment(EventsFragment.newInstance());
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Prefs.with(this).clear();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (lastSelectedMenuItemId == item.getItemId()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        boolean isFragment;

        switch (item.getItemId()) {
            case R.id.nav_events:
                setTitle(item.getTitle());
                addFragment(EventsFragment.newInstance());
                fab.show();
                isFragment = true;
                break;
            case R.id.nav_subscribed_events:
                setTitle(item.getTitle());
                addFragment(SubscribedEventsFragment.newInstance());
                fab.hide();
                isFragment = true;
                break;
            case R.id.nav_detention_alert:
                pendingIntent = new Intent(this, DetentionAlertActivity.class);
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
        fragmentTransaction.replace(R.id.content_main, newFragment);
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

    @OnClick(R.id.fab)
    @Override
    public void createEventAction() {
        startActivity(new Intent(MainActivity.this, CreateEventActivity.class));
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}