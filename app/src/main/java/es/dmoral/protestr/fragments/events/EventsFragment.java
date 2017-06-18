package es.dmoral.protestr.fragments.events;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.adapters.EventAdapter;
import es.dmoral.protestr.api.models.Event;
import es.dmoral.protestr.base.BaseFragment;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.toasty.Toasty;

public class EventsFragment extends BaseFragment implements EventsFragmentView {

    @BindView(R.id.events_recyclerview) RecyclerView eventsRecyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    private EventsPresenter eventsPresenter;
    private int scrollPage = 1;
    private boolean allEventsLoaded = false;
    private boolean loading = true;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventsPresenter = new EventsPresenterImpl(this);
        return super.onCreateView(inflater, R.layout.fragment_events, container, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        setHasOptionsMenu(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setAdapter(new EventAdapter(new ArrayList<Event>()));
        swipeRefreshLayout.setRefreshing(true);
        eventsPresenter.getNewEvents(0, Constants.EVENT_LIMIT_CALL,
                Prefs.with(getActivity()).read(Constants.PREFERENCES_ORDER_BY, Constants.ORDER_CREATION_DATE_DESC));
    }

    @Override
    protected void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollPage = 1;
                loading = true;
                ((EventAdapter) eventsRecyclerView.getAdapter()).clearAll();
                eventsPresenter.getNewEvents(0, Constants.EVENT_LIMIT_CALL,
                        Prefs.with(getActivity()).read(Constants.PREFERENCES_ORDER_BY, Constants.ORDER_CREATION_DATE_DESC));
            }
        });
        eventsRecyclerView.addOnScrollListener(new InfiniteScrollListener(Constants.EVENT_LIMIT_CALL, (LinearLayoutManager) eventsRecyclerView.getLayoutManager()) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                if (!allEventsLoaded && !loading) {
                    loading = true;
                    ((EventAdapter) eventsRecyclerView.getAdapter()).showProgress();
                    int offset = scrollPage++ * Constants.EVENT_LIMIT_CALL;
                        eventsPresenter.getNewEvents(offset, Constants.EVENT_LIMIT_CALL,
                            Prefs.with(getActivity()).read(Constants.PREFERENCES_ORDER_BY, Constants.ORDER_CREATION_DATE_DESC));
                }
            }
        });
    }

    @Override
    public void populateNewEventList(ArrayList<Event> newEvents) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        ((EventAdapter) eventsRecyclerView.getAdapter()).removeProgress();
        loading = false;
        if (newEvents != null && newEvents.size() != 0) {
            ((EventAdapter) eventsRecyclerView.getAdapter()).addNewItems(newEvents);
            if (allEventsLoaded)
                allEventsLoaded = false;
        } else {
            allEventsLoaded = true;
            Toasty.info(getActivity(), getString(R.string.no_more_events_toast)).show();
        }
    }

    @Override
    public void setFirstMenuItemChecked(Menu menu) {
        final String order = Prefs.with(getActivity()).read(Constants.PREFERENCES_ORDER_BY,
                Constants.ORDER_CREATION_DATE_DESC);
        switch (order) {
            case Constants.ORDER_CREATION_DATE_ASC:
                menu.findItem(R.id.sort_by_oldest).setChecked(true);
                break;
            case Constants.ORDER_CREATION_DATE_DESC:
                menu.findItem(R.id.sort_by_newest).setChecked(true);
                break;
            case Constants.ORDER_FROM_PARTICIPANTS_DESC:
                menu.findItem(R.id.sort_by_participants).setChecked(true);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_events, menu);
        setFirstMenuItemChecked(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String order;
        switch (item.getItemId()) {
            case R.id.sort_by_participants:
                order = Constants.ORDER_FROM_PARTICIPANTS_DESC;
                break;
            case R.id.sort_by_newest:
                order = Constants.ORDER_CREATION_DATE_DESC;
                break;
            case R.id.sort_by_oldest:
                order = Constants.ORDER_CREATION_DATE_ASC;
                break;
            default:
                return false;
        }
        scrollPage = 1;
        loading = true;
        swipeRefreshLayout.setRefreshing(true);
        ((EventAdapter) eventsRecyclerView.getAdapter()).clearAll();
        eventsPresenter.getNewEvents(0, Constants.EVENT_LIMIT_CALL, order);
        item.setChecked(true);
        Prefs.with(getActivity()).write(Constants.PREFERENCES_ORDER_BY, order);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventsPresenter != null)
            eventsPresenter.onDestroy();
    }
}
