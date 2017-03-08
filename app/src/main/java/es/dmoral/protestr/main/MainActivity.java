package es.dmoral.protestr.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.fragments.events.EventsFragment;
import es.dmoral.protestr.login.LoginActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.prefs.Prefs;
import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private LinearLayout headerView;

    private int lastSelectedMenuItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
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

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        headerView = (LinearLayout) navigationView.getHeaderView(0);
        headerView.findViewById(R.id.navigation_header_container).setBackgroundResource(R.drawable.nav_header_background);
        ((TextView) headerView.findViewById(R.id.nav_title)).setText(Prefs.with(this).read(Constants.PREFERENCES_USERNAME));
        setTitle(navigationView.getMenu().findItem(R.id.nav_events).getTitle());
        navigationView.getMenu().findItem(R.id.nav_events).setCheckable(true);
        navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
        lastSelectedMenuItemId = R.id.nav_events;
        addFragment(true, EventsFragment.newInstance());
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
                Prefs.with(this).writeBoolean(Constants.PREFERENCES_LOGGED_IN, false);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
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

        switch (item.getItemId()) {
            case R.id.nav_events:
                setTitle(item.getTitle());
                addFragment(false, EventsFragment.newInstance());
                fab.show();
                break;
            case R.id.nav_subscribed_events:
                setTitle(item.getTitle());
                fab.hide();
                break;
            case R.id.nav_detention_alert:
                break;
        }

        lastSelectedMenuItemId = item.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addFragment(boolean firstFragment, @NonNull Fragment newFragment) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (firstFragment)
            fragmentTransaction.add(R.id.content_main, newFragment);
        else
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
}
