package es.dmoral.protestr.fragments.events;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.api.models.Event;
import es.dmoral.protestr.utils.Constants;

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
    public void getNewEvents(int offset, int limit) {
        String iso3 = null;
        if (!Prefs.with(((EventsFragment) eventsFragmentView).getContext()).readBoolean(Constants.PREFERENCES_SHOW_ALL_EVENTS_LOCATION))
            iso3 = Prefs.with(((EventsFragment) eventsFragmentView).getContext()).read(Constants.PREFERENCES_SELECTED_COUNTRY);
        eventsInteractor.getNewEvents(this, iso3, offset, limit);
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
