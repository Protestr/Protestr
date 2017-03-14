package es.dmoral.protestr.fragments.events;

import android.util.Log;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.api.models.Event;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.LocaleUtils;

/**
 * Created by grender on 16/02/17.
 */

public class EventsPresenterImpl implements EventsPresenter, EventsInteractor.OnGetNewEventsListener {

    private EventsFragmentView eventsFragmentView;
    private EventsInteractor eventsInteractor;

    EventsPresenterImpl(EventsFragmentView eventsFragmentView) {
        this.eventsFragmentView = eventsFragmentView;
        this.eventsInteractor = new EventsInteractorImpl();
    }

    @Override
    public void getNewEvents(int offset, int limit, String order) {
        String iso3 = null;
        if (Prefs.with(((EventsFragment) eventsFragmentView).getContext())
                .read(Constants.PREFERENCES_FILTER_LOCATION_EVENTS, ((EventsFragment) eventsFragmentView).getString(R.string.filter_location_events_default_value))
                .equals(((EventsFragment) eventsFragmentView).getString(R.string.filter_location_events_default_value))) {
            iso3 = Prefs.with(((EventsFragment) eventsFragmentView)
                    .getContext())
                    .read(Constants.PREFERENCES_SELECTED_COUNTRY, LocaleUtils.getDeviceLocale(((EventsFragment) eventsFragmentView).getContext()));
        }
        eventsInteractor.getNewEvents(this, iso3, offset, limit, order);
    }

    @Override
    public void onNewEventsReceived(ArrayList<Event> newEvents) {
        eventsFragmentView.populateNewEventList(newEvents);
    }

    @Override
    public void onNewEventsError() {

    }

    @Override
    public void onDestroy() {
        eventsFragmentView = null;
    }
}
