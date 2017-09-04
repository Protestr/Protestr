package es.dmoral.protestr.ui.activities.event_info;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;


import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.Event;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.custom.PaletteBitmap;
import es.dmoral.protestr.utils.Constants;

public class EventInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.event_image) ImageView eventImage;

    private Event event;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        event = getIntent().getParcelableExtra(Constants.EVENT_INFO_EXTRA);
        super.onCreate(savedInstanceState, R.layout.activity_event_info);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(event.getTitle());
        Glide.with(this)
                .fromString()
                .asBitmap()
                .transcode(new PaletteBitmap.PaletteBitmapTranscoder(this), PaletteBitmap.class)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(event.getImageUrl())
                .into(new ImageViewTarget<PaletteBitmap>(eventImage) {
                    @Override
                    protected void setResource(PaletteBitmap resource) {
                        eventImage.setImageBitmap(resource.bitmap);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(resource.palette.getDarkMutedColor(
                                    ContextCompat.getColor(EventInfoActivity.this, R.color.colorPrimaryDark)));
                        }

                        collapsingToolbarLayout.setContentScrimColor(resource.palette.getMutedColor(
                                ContextCompat.getColor(EventInfoActivity.this, R.color.colorPrimary))
                        );
                        collapsingToolbarLayout.setStatusBarScrimColor(resource.palette.getDarkMutedColor(
                                ContextCompat.getColor(EventInfoActivity.this, R.color.colorPrimaryDark))
                        );
                    }
                });
    }

    @Override
    protected void setListeners() {

    }
}
