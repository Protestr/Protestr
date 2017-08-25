package es.dmoral.protestr.ui.fragments.events;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.Event;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.LocaleUtils;
import es.dmoral.protestr.utils.PreferencesUtils;
import im.delight.android.location.SimpleLocation;

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
        double lat = -1;
        double lng = -1;

        if (order.equals(Constants.ORDER_DISTANCE_ASC)) {
            final SimpleLocation simpleLocation = new SimpleLocation(((EventsFragment) eventsFragmentView).getContext());
            simpleLocation.beginUpdates();
            lat = simpleLocation.getLatitude();
            lng = simpleLocation.getLongitude();
            simpleLocation.endUpdates();
        }

        if (Prefs.with(((EventsFragment) eventsFragmentView).getContext())
                .read(PreferencesUtils.PREFERENCES_FILTER_LOCATION_EVENTS, ((EventsFragment) eventsFragmentView).getString(R.string.filter_location_events_default_value))
                .equals(((EventsFragment) eventsFragmentView).getString(R.string.filter_location_events_default_value))) {
            iso3 = Prefs.with(((EventsFragment) eventsFragmentView)
                    .getContext())
                    .read(PreferencesUtils.PREFERENCES_SELECTED_COUNTRY, LocaleUtils.getDeviceLocale(((EventsFragment) eventsFragmentView).getContext()));
        }

        eventsInteractor.getNewEvents(this, iso3, offset, limit, order, lat, lng);
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
