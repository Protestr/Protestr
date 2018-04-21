package org.protestr.app.ui.fragments.subscribed_events;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.event_info.EventInfoActivity;
import org.protestr.app.ui.adapters.SubscribedEventAdapter;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.PreferencesUtils;

import java.util.ArrayList;

import butterknife.BindView;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.event_info.EventInfoActivity;
import org.protestr.app.ui.adapters.SubscribedEventAdapter;
import org.protestr.app.ui.fragments.BaseFragment;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.PreferencesUtils;
import es.dmoral.toasty.Toasty;

public class SubscribedEventsFragment extends BaseFragment implements SubscribedEventsView, SubscribedEventAdapter.OnSubscribedEventClickedListener {

    @BindView(org.protestr.app.R.id.subscribed_events_recyclerview)
    RecyclerView subscribedEventsRecyclerView;
    @BindView(org.protestr.app.R.id.subscribed_events_swipe_container)
    SwipeRefreshLayout subscribedEventSwipeRefreshLayout;

    private SubscribedEventsPresenter subscribedEventsPresenter;

    public static SubscribedEventsFragment newInstance() {
        return new SubscribedEventsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        subscribedEventsPresenter = new SubscribedEventsPresenterImpl(this);
        return super.onCreateView(inflater, org.protestr.app.R.layout.fragment_subscribed_events, container, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        subscribedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscribedEventsRecyclerView.setAdapter(new SubscribedEventAdapter(this));
        subscribedEventSwipeRefreshLayout.setRefreshing(true);
        final User user = PreferencesUtils.getLoggedUser(getContext());
        subscribedEventsPresenter.getSubscribedEvents(user.getEmail(), user.getPassword());
    }

    @Override
    protected void setListeners() {
        subscribedEventSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final User user = PreferencesUtils.getLoggedUser(getContext());
                subscribedEventsPresenter.getSubscribedEvents(user.getEmail(), user.getPassword());
            }
        });
    }

    @Override
    public void populateSubscribedEvents(ArrayList<Event> subscribedEvents) {
        if (subscribedEventSwipeRefreshLayout.isRefreshing())
            subscribedEventSwipeRefreshLayout.setRefreshing(false);
        ((SubscribedEventAdapter) subscribedEventsRecyclerView.getAdapter()).addItems(subscribedEvents);
    }

    @Override
    public void showError() {
        Toasty.error(getContext(), getString(org.protestr.app.R.string.error_getting_subscribed_events)).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.EVENT_REQUEST_CODE:
                    final Event event = data.getParcelableExtra(Constants.EVENT_INFO_EXTRA);
                    if (event.getParticipants() == -1 || !event.isSubscribed())
                        ((SubscribedEventAdapter) subscribedEventsRecyclerView.getAdapter()).removeItem(event);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscribedEventsPresenter.onDestroy();
    }

    @Override
    public void onEventClicked(Event event) {
        final Intent eventInfoIntent = new Intent(getActivity(), EventInfoActivity.class);
        eventInfoIntent.putExtra(Constants.EVENT_INFO_EXTRA, event);
        startActivityForResult(eventInfoIntent, Constants.EVENT_REQUEST_CODE);
        getActivity().overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
    }
}
