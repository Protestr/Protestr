package org.protestr.app.ui.activities.event_info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.event_info.image_viewer.ImageViewerActivity;
import org.protestr.app.ui.activities.live_feed.LiveFeedActivity;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.FormatUtils;
import org.protestr.app.utils.ImageUtils;
import org.protestr.app.utils.PreferencesUtils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import org.protestr.app.R;
import org.protestr.app.ui.activities.BaseActivity;

public class EventInfoActivity extends BaseActivity implements EventInfoView, OnMapReadyCallback {

    @BindView(org.protestr.app.R.id.toolbar)
    Toolbar toolbar;
    @BindView(org.protestr.app.R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(org.protestr.app.R.id.event_image_container)
    RelativeLayout eventImageContainer;
    @BindView(org.protestr.app.R.id.event_image)
    ImageView eventImage;
    @BindView(org.protestr.app.R.id.tv_event_name)
    TextView eventName;
    @BindView(org.protestr.app.R.id.tv_event_description)
    TextView eventDescription;
    @BindView(org.protestr.app.R.id.tv_date)
    TextView eventDate;
    @BindView(org.protestr.app.R.id.tv_participants)
    TextView eventParticipants;
    @BindView(org.protestr.app.R.id.tv_event_location)
    TextView eventLocation;
    @BindView(org.protestr.app.R.id.map_view)
    MapView mapView;
    @BindView(org.protestr.app.R.id.subscribe_layout)
    LinearLayout subscribeLayout;
    @BindView(org.protestr.app.R.id.unsubscribe_layout)
    LinearLayout unsubscribeLayout;
    @BindView(org.protestr.app.R.id.delete_layout)
    LinearLayout deleteLayout;
    @BindView(org.protestr.app.R.id.qr_code_layout)
    LinearLayout qrCodeLayout;
    @BindView(org.protestr.app.R.id.qr_icon)
    ImageView qrIcon;
    @BindView(org.protestr.app.R.id.qr_progress)
    ProgressBar qrProgress;

    private static final String MAP_VIEW_SAVED_STATE = "MAP_VIEW_STATE";

    private Event event;
    private GoogleMap googleMap;
    private EventInfoPresenterImpl eventInfoPresenter;
    private MaterialDialog progressDialog;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        event = getIntent().getParcelableExtra(Constants.EVENT_INFO_EXTRA);
        super.onCreate(savedInstanceState, org.protestr.app.R.layout.activity_event_info);
        eventInfoPresenter = new EventInfoPresenterImpl(this);

        Bundle mapViewSavedInstanceState = null;
        if (savedInstanceState != null)
            mapViewSavedInstanceState = savedInstanceState.getBundle(MAP_VIEW_SAVED_STATE);
        if (mapView != null)
            mapView.onCreate(mapViewSavedInstanceState);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView.getMapAsync(this);

        setTitle(event.getTitle());
        Glide.with(this)
                .asBitmap()
                .load(event.getImageUrl())
                .apply(new RequestOptions()
                        .skipMemoryCache(true)) // Prevent animation bug
                .into(new ImageViewTarget<Bitmap>(eventImage) {
                    @Override
                    protected void setResource(@Nullable final Bitmap resource) {
                        Animation alphaAnimation = new AlphaAnimation(0f, 1f);
                        alphaAnimation.setDuration(500);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                eventImage.setImageBitmap(resource);
                                eventImageContainer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        eventImageContainer.startAnimation(alphaAnimation);

                        if (resource != null && !resource.isRecycled()) {
                            Palette palette = Palette.from(resource).generate();

                            collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(
                                    ContextCompat.getColor(EventInfoActivity.this, org.protestr.app.R.color.colorPrimary))
                            );
                        }
                    }
                });
        eventName.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
        eventDate.setText(FormatUtils.formatDateByDefaultLocale(event.getFromDate()) + " - " +
                FormatUtils.formatHours(event.getFromDate()));
        eventParticipants.setText(String.valueOf(event.getParticipants()));
        eventLocation.setText(event.getLocationName());

        if (PreferencesUtils.getLoggedUser(this).getId().equals(event.getUserId())) {
            deleteLayout.setVisibility(View.VISIBLE);
        } else if (event.isSubscribed()) {
            unsubscribeLayout.setVisibility(View.VISIBLE);
        } else {
            subscribeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        moveCameraBackToLocation(true);
    }

    // As seen in http://stackoverflow.com/a/39525123/4208583
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //This MUST be done before saving any of your own or your base class's variables
        final Bundle mapViewSaveState = new Bundle(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle(MAP_VIEW_SAVED_STATE, mapViewSaveState);
        //Add any other variables here.
        super.onSaveInstanceState(outState);
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
        if (mapView != null)
            mapView.onDestroy();
        eventInfoPresenter.onDestroy();
    }

    @OnClick(org.protestr.app.R.id.tv_event_location)
    @Override
    public void moveCameraBackToLocation() {
        moveCameraBackToLocation(false);
    }

    @Override
    public void moveCameraBackToLocation(boolean firstTime) {
        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(12f)
                .build();
        if (firstTime) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.addMarker(new MarkerOptions().position(latLng));
        } else {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @OnClick(org.protestr.app.R.id.toolbar_layout)
    @Override
    public void imageClicked() {
        Glide.with(this)
                .asBitmap()
                .load(event.getImageUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        openImageViewer(resource);
                    }
                });
    }

    @Override
    public void openImageViewer(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        Intent viewerIntent = new Intent(this, ImageViewerActivity.class);
        viewerIntent.putExtra(Constants.IMAGE_VIEWER_EXTRA, byteArrayOutputStream.toByteArray());
        startActivity(viewerIntent);
        overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
    }

    @Override
    public void showProgress(@StringRes int message) {
        progressDialog = new MaterialDialog.Builder(this)
                .content(message)
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

    @OnClick(org.protestr.app.R.id.subscribe_layout)
    @Override
    public void join() {
        showProgress(org.protestr.app.R.string.joining);
        final User user = PreferencesUtils.getLoggedUser(this);
        eventInfoPresenter.joinEvent(user.getEmail(), user.getPassword(), event.getEventId());
    }

    @Override
    public void onEventJoined() {
        event.setSubscribed(true);
        event.setParticipants(event.getParticipants() + 1);
        eventParticipants.setText(String.valueOf(event.getParticipants()));
        subscribeLayout.setVisibility(View.GONE);
        unsubscribeLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(org.protestr.app.R.id.unsubscribe_layout)
    @Override
    public void leave() {
        showProgress(org.protestr.app.R.string.leaving);
        final User user = PreferencesUtils.getLoggedUser(this);
        eventInfoPresenter.leaveEvent(user.getEmail(), user.getPassword(), event.getEventId(),
                event.isAdmin());
    }

    @Override
    public void onEventLeft() {
        event.setSubscribed(false);
        event.setParticipants(event.getParticipants() - 1);
        eventParticipants.setText(String.valueOf(event.getParticipants()));
        subscribeLayout.setVisibility(View.VISIBLE);
        unsubscribeLayout.setVisibility(View.GONE);
    }

    @OnClick(org.protestr.app.R.id.delete_layout)
    @Override
    public void delete() {
        new MaterialDialog.Builder(this)
                .title(org.protestr.app.R.string.delete_event)
                .content(org.protestr.app.R.string.delete_event_confirmation)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress(org.protestr.app.R.string.deleting);
                        final User user = PreferencesUtils.getLoggedUser(EventInfoActivity.this);
                        eventInfoPresenter.deleteEvent(user.getEmail(), user.getPassword(), event.getEventId());
                    }
                })
                .show();
    }

    @OnClick(R.id.fab)
    @Override
    public void openLiveFeed() {
        if (event.isSubscribed()) {
            Intent liveFeedIntent = new Intent(this, LiveFeedActivity.class);
            liveFeedIntent.putExtra(Constants.EVENT_INFO_EXTRA, event);
            startActivity(liveFeedIntent);
            overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
        } else {
            Toasty.info(this, getString(R.string.live_feed_event_not_subscribed), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onEventDeleted() {
        event.setParticipants(-1);
        onBackPressed();
    }

    @OnClick(org.protestr.app.R.id.qr_code_layout)
    @Override
    public void generateQr() {
        qrCodeLayout.setClickable(false);
        qrIcon.setVisibility(View.GONE);
        qrProgress.setVisibility(View.VISIBLE);
        ImageUtils.generateQr(new ImageUtils.OnQrGeneratedListener() {
            @Override
            public void onQrGenerated(Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrCodeLayout.setClickable(true);
                        qrIcon.setVisibility(View.VISIBLE);
                        qrProgress.setVisibility(View.GONE);
                    }
                });
                openImageViewer(bitmap);
            }
        }, event.getEventId());
    }

    @Override
    public void onBackPressed() {
        final Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EVENT_INFO_EXTRA, event);
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
    }
}
