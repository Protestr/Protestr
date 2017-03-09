package es.dmoral.protestr.fragments.events;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.adapters.EventAdapter;
import es.dmoral.protestr.api.models.Event;
import es.dmoral.protestr.base.BaseFragment;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.toasty.Toasty;

public class EventsFragment extends BaseFragment implements EventsFragmentView {

    @BindView(R.id.events_recyclerview) RecyclerView eventsRecyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

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
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setAdapter(new EventAdapter(new ArrayList<Event>()));
        swipeRefreshLayout.setRefreshing(true);
        eventsPresenter.getNewEvents(0, Constants.EVENT_LIMIT_CALL);
    }

    @Override
    protected void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollPage = 1;
                loading = true;
                ((EventAdapter) eventsRecyclerView.getAdapter()).clearAll();
                eventsPresenter.getNewEvents(0, Constants.EVENT_LIMIT_CALL);
            }
        });
        eventsRecyclerView.addOnScrollListener(new InfiniteScrollListener(Constants.EVENT_LIMIT_CALL, (LinearLayoutManager) eventsRecyclerView.getLayoutManager()) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                if (!allEventsLoaded && !loading) {
                    loading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    int offset = scrollPage++ * Constants.EVENT_LIMIT_CALL;
                    eventsPresenter.getNewEvents(offset, Constants.EVENT_LIMIT_CALL);
                }
            }
        });
    }

    @Override
    public void populateNewEventList(ArrayList<Event> newEvents) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        if (progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.GONE);
        loading = false;
        if (newEvents.size() != 0) {
            ((EventAdapter) eventsRecyclerView.getAdapter()).addNewItems(newEvents);
            if (allEventsLoaded)
                allEventsLoaded = false;
        } else {
            allEventsLoaded = true;
            Toasty.info(getActivity(), getString(R.string.no_more_events_toast)).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsPresenter.onDestroy();
    }
}
