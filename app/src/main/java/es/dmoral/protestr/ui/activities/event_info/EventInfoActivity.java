package es.dmoral.protestr.ui.activities.event_info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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


import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.Event;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.activities.event_info.image_viewer.ImageViewerActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.FormatUtils;
import es.dmoral.protestr.utils.ImageUtils;

public class EventInfoActivity extends BaseActivity implements EventInfoView, OnMapReadyCallback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.event_image_container) RelativeLayout eventImageContainer;
    @BindView(R.id.event_image) ImageView eventImage;
    @BindView(R.id.tv_event_name) TextView eventName;
    @BindView(R.id.tv_event_description) TextView eventDescription;
    @BindView(R.id.tv_date) TextView eventDate;
    @BindView(R.id.tv_participants) TextView eventParticipants;
    @BindView(R.id.tv_event_location) TextView eventLocation;
    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.subscribe_layout) LinearLayout subscribeLayout;
    @BindView(R.id.qr_icon) ImageView qrIcon;
    @BindView(R.id.qr_progress) ProgressBar qrProgress;

    private static final String MAP_VIEW_SAVED_STATE = "MAP_VIEW_STATE";

    private Event event;
    private GoogleMap googleMap;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        event = getIntent().getParcelableExtra(Constants.EVENT_INFO_EXTRA);
        super.onCreate(savedInstanceState, R.layout.activity_event_info);
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
                                    ContextCompat.getColor(EventInfoActivity.this, R.color.colorPrimary))
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
    }

    @OnClick(R.id.tv_event_location)
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

    @OnClick(R.id.toolbar_layout)
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        Intent viewerIntent = new Intent(this, ImageViewerActivity.class);
        viewerIntent.putExtra(Constants.IMAGE_VIEWER_EXTRA, byteArrayOutputStream.toByteArray());
        startActivity(viewerIntent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @OnClick(R.id.subscribe_layout)
    @Override
    public void subscribe() {

    }

    @OnClick(R.id.qr_code_layout)
    @Override
    public void generateQr() {
        qrIcon.setVisibility(View.GONE);
        qrProgress.setVisibility(View.VISIBLE);
        ImageUtils.generateQr(new ImageUtils.OnQrGeneratedListener() {
            @Override
            public void onQrGenerated(Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrIcon.setVisibility(View.VISIBLE);
                        qrProgress.setVisibility(View.GONE);
                    }
                });
                openImageViewer(bitmap);
            }
        }, event.getEventId());
    }
}
