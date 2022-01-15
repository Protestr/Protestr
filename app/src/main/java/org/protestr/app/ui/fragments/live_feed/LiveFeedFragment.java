package org.protestr.app.ui.fragments.live_feed;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.EventUpdate;
import org.protestr.app.ui.adapters.LiveUpdateAdapter;
import org.protestr.app.ui.custom.MarginItemDecorator;
import org.protestr.app.ui.fragments.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class LiveFeedFragment extends BaseFragment implements LiveFeedFragmentView {

    @BindView(R.id.live_feed_container)
    RelativeLayout liveFeedContainer;
    @BindView(R.id.event_updates_recyclerview)
    RecyclerView eventUpdatesRecyclerview;

    private static final String EVENT_ID_EXTRA = "EVENT_ID_EXTRA";
    private static final String IS_ADMIN_EXTRA = "IS_ADMIN_EXTRA";

    private String eventId;
    private boolean isAdmin;

    public LiveFeedFragment() {
        // required empty constructor
    }

    public static LiveFeedFragment newInstance(String eventId, boolean isAdmin) {
        LiveFeedFragment liveFeedFragment = new LiveFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_ID_EXTRA, eventId);
        bundle.putBoolean(IS_ADMIN_EXTRA, isAdmin);
        liveFeedFragment.setArguments(bundle);
        return liveFeedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(EVENT_ID_EXTRA))
                eventId = getArguments().getString(EVENT_ID_EXTRA);
            if (getArguments().containsKey(IS_ADMIN_EXTRA))
                isAdmin = getArguments().getBoolean(IS_ADMIN_EXTRA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_live_feed, container, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        eventUpdatesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        eventUpdatesRecyclerview.addItemDecoration(new MarginItemDecorator(eventUpdatesRecyclerview.getContext(),
                12 + 48 + 16));
        // Testing
        /*ArrayList<EventUpdate> eventUpdates = new ArrayList<>();
        eventUpdates.add(new EventUpdate("adad", "Pepito", "https://www.gravatar.com/avatar/73a0c1583d5fcc2fc04631a1eb63fdc4?s=512&d=retro", "hola como estas?",
                "Esto es una prueba! A ver qué tal va \uD83D\uDE0A", true, 1524616958645L));
        eventUpdates.add(new EventUpdate("adad", "Manolo", "https://www.gravatar.com/avatar/73a0c1583d5fcc2fc04631a1eb63fde5?s=512&d=retro", "bien y tu?",
                "Esto es una prueba! A ver qué tal va \uD83D\uDE0A", true, 1524616958645L));
        eventUpdates.add(new EventUpdate("adad", "Pepito", "https://www.gravatar.com/avatar/73a0c1583d5fcc2fc04631a1eb63fdc4?s=512&d=retro", "aqui, nos estan zurrando un poco",
                "Esto es una prueba! A ver qué tal va \uD83D\uDE0A", true, 1524616958645L));
        eventUpdates.add(new EventUpdate("adad", "Manolo", "https://www.gravatar.com/avatar/73a0c1583d5fcc2fc04631a1eb63fde5?s=512&d=retro", "aah bueno, nada nuevo por alli pues",
                "Esto es una prueba! A ver qué tal va \uD83D\uDE0A", true, 1524616958645L));
        eventUpdates.add(new EventUpdate("adad", "Pepito", "https://www.gravatar.com/avatar/73a0c1583d5fcc2fc04631a1eb63fdc4?s=512&d=retro",
                "Esto es una prueba! Voy a intentar hacer este texto un poco más largo, a ver si se hace mas largo que la imagen... Ya veremos, aun no estoy seguro de si me convence o no. A ver qué tal va \uD83D\uDE0A", "1", true, 1524616958645L));
        eventUpdates.add(new EventUpdate("adad", "Jandrete", "https://www.gravatar.com/avatar/73a0c1583d5fcc2gh04631a1eb63fdc4?s=512&d=retro", "\n" +
                "\uD83D\uDCB5\uD83D\uDC22\uD83D\uDD65\uD83C\uDF5A\uD83D\uDC2E\uD83D\uDD37 \uD83D\uDC1D\uD83D\uDCA1\uD83D\uDD58\uD83D\uDCF7\uD83D\uDD59 \uD83D\uDD0E\uD83D\uDCDE\uD83D\uDCA3\uD83C\uDFC8\uD83C\uDFE1\uD83C\uDF70\uD83D\uDC10\uD83D\uDC70",
                "1", true, 1524616958985L));*/
        eventUpdatesRecyclerview.setAdapter(new LiveUpdateAdapter(new ArrayList<EventUpdate>()));
    }

    @Override
    protected void setListeners() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newUpdateReceived(EventUpdate eventUpdate) {
        if (eventUpdate.getEventId().equals(eventId)) {
            if (eventUpdate.isFromAdmin() == isAdmin) {
                ((LiveUpdateAdapter) eventUpdatesRecyclerview.getAdapter()).addItem(eventUpdate);
                eventUpdatesRecyclerview.smoothScrollToPosition(eventUpdatesRecyclerview.getAdapter().getItemCount() - 1);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
