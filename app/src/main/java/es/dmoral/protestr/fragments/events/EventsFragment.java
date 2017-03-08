package es.dmoral.protestr.fragments.events;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.adapters.EventAdapter;
import es.dmoral.protestr.api.models.Event;
import es.dmoral.protestr.base.BaseFragment;
import es.dmoral.protestr.main.MainActivity;
import es.dmoral.protestr.utils.LocaleUtils;

public class EventsFragment extends BaseFragment implements EventsFragmentView {
    @BindView(R.id.events_recyclerview) RecyclerView eventsRecyclerView;

    private EventsPresenter eventsPresenter;

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
        eventsPresenter.getNewEvents();
    }

    @Override
    protected void setListeners() {
    }

    @Override
    public void populateNewEventList(ArrayList<Event> newEvents) {
        eventsRecyclerView.setAdapter(new EventAdapter(newEvents));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsPresenter.onDestroy();
    }
}
