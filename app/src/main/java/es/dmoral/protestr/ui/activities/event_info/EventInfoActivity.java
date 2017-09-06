package es.dmoral.protestr.ui.activities.event_info;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;


import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.Event;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.utils.Constants;

public class EventInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.event_image_container) RelativeLayout eventImageContainer;
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
    }

    @Override
    protected void setListeners() {

    }
}
