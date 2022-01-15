package org.protestr.app.ui.activities.live_feed;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;

import org.protestr.app.R;
import org.protestr.app.data.fcm.ProtestrMessagingService;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.BaseActivity;
import org.protestr.app.ui.adapters.LiveUpdateAdapter;
import org.protestr.app.ui.custom.SimplifiedTextWatcher;
import org.protestr.app.ui.fragments.live_feed.LiveFeedFragment;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LiveFeedActivity extends BaseActivity implements LiveFeedView {

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.feed_container)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.live_feed_emoji_bar)
    LinearLayout liveFeedEmojiBar;
    @BindView(R.id.et_emoji)
    EmojiEditText emojiEditText;
    @BindView(R.id.live_feed_content)
    CoordinatorLayout liveFeedContent;
    @BindView(R.id.send_message)
    ImageView sendMessage;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private Event event;
    private User user;

    private LiveFeedPresenter liveFeedPresenter;
    private Menu currentMenu;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        event = getIntent().getParcelableExtra(Constants.EVENT_INFO_EXTRA);
        ProtestrMessagingService.currentShownEventId = event.getEventId();
        super.onCreate(savedInstanceState, R.layout.activity_live_feed);

        user = PreferencesUtils.getLoggedUser(this);
        liveFeedPresenter = new LiveFeedPresenterImpl(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_live_feed, menu);
        this.currentMenu = menu;
        if (PreferencesUtils.isEventMuted(this, event.getEventId()))
            menu.findItem(R.id.action_mute).setVisible(false);
        else
            menu.findItem(R.id.action_unmute).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mute:
                PreferencesUtils.muteEvent(this, event.getEventId());
                item.setVisible(false);
                if (currentMenu != null)
                    currentMenu.findItem(R.id.action_unmute).setVisible(true);
                break;
            case R.id.action_unmute:
                PreferencesUtils.unmuteEvent(this, event.getEventId());
                item.setVisible(false);
                if (currentMenu != null)
                    currentMenu.findItem(R.id.action_mute).setVisible(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(event.getTitle());
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,
                    typedValue, true);

            sendMessage.setBackgroundResource(typedValue.resourceId);
        }

        viewPager.setCurrentItem(event.isAdmin() ? 0 : 1);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListeners() {
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                KeyboardUtils.closeKeyboard(emojiEditText);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                animateTextBar(position == 0 && !event.isAdmin() || position == 1 && event.isAdmin(),
                        position);
            }
        });
        emojiEditText.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    sendMessage.setClickable(false);
                    sendMessage.setAlpha(0.2f);
                } else {
                    sendMessage.setClickable(true);
                    sendMessage.setAlpha(0.5f);
                }
            }
        });
        emojiEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                appBarLayout.setExpanded(false, true);
                return false;
            }
        });
    }

    @OnClick(R.id.send_message)
    @Override
    public void sendEventUpdate() {
        final String message = emojiEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            liveFeedPresenter.postUpdate(user.getEmail(), user.getPassword(), event.getEventId(),
                    event.getTitle(), message);
            emojiEditText.setText(null);
        }
    }

    @Override
    public void setTextBarVisibility(int position) {
        switch (position) {
            case 0: // admin
                liveFeedEmojiBar.setVisibility(event.isAdmin() ? View.VISIBLE : View.GONE);
                break;
            case 1: // all
                liveFeedEmojiBar.setVisibility(event.isAdmin() ? View.GONE : View.VISIBLE);
                break;

        }
    }

    @Override
    public void animateTextBar(boolean isHiding, final int position) {
        TranslateAnimation translateContent;
        if (isHiding) {
            translateContent =
                    new TranslateAnimation(0, 0, 0, liveFeedEmojiBar.getHeight());
            translateContent.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewPager.setPadding(0, 0, 0, 0);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setTextBarVisibility(position);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // unused
                }
            });
        } else {
            liveFeedEmojiBar.setVisibility(View.VISIBLE);
            translateContent =
                    new TranslateAnimation(0, 0, liveFeedEmojiBar.getHeight(), 0);
            translateContent.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    setTextBarVisibility(position);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    viewPager.setPadding(0, 0, 0,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 58, getResources().getDisplayMetrics()));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // unused
                }
            });
        }
        translateContent.setFillAfter(true);
        translateContent.setDuration(200);
        liveFeedEmojiBar.startAnimation(translateContent);
    }

    @Override
    protected void onDestroy() {
        if (currentMenu != null)
            currentMenu = null;
        ProtestrMessagingService.currentShownEventId = null;
        super.onDestroy();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LiveFeedFragment.newInstance(event.getEventId(), position == 0);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_text_admin);
                case 1:
                    return getString(R.string.tab_text_all);
            }
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
