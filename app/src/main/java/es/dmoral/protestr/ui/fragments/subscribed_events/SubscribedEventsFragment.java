package es.dmoral.protestr.ui.fragments.subscribed_events;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.dmoral.protestr.R;
import es.dmoral.protestr.ui.fragments.BaseFragment;

public class SubscribedEventsFragment extends BaseFragment {

    public static SubscribedEventsFragment newInstance() {
        return new SubscribedEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, R.layout.fragment_subscribed_events, container, savedInstanceState);
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void setListeners() {

    }
}