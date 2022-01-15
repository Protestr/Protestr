package org.protestr.app.ui.fragments.events;

import android.Manifest;

import org.protestr.app.data.models.dao.Event;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.LocaleUtils;
import org.protestr.app.utils.PermissionUtils;
import org.protestr.app.utils.PreferencesUtils;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.LocaleUtils;
import org.protestr.app.utils.PermissionUtils;
import org.protestr.app.utils.PreferencesUtils;
import im.delight.android.location.SimpleLocation;

/**
 * Created by someone on 16/02/17.
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
        String userId = PreferencesUtils.getLoggedUser(((EventsFragment) eventsFragmentView).getContext()).getId();
        if (order.equals(Constants.ORDER_DISTANCE_ASC) && PermissionUtils.isPermissionGranted(
                ((EventsFragment) eventsFragmentView).getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            final SimpleLocation simpleLocation = new SimpleLocation(((EventsFragment) eventsFragmentView).getContext());
            simpleLocation.beginUpdates();
            lat = simpleLocation.getLatitude();
            lng = simpleLocation.getLongitude();
            simpleLocation.endUpdates();
        }

        if (Prefs.with(((EventsFragment) eventsFragmentView).getContext())
                .read(PreferencesUtils.PREFERENCES_FILTER_LOCATION_EVENTS, ((EventsFragment) eventsFragmentView).getString(org.protestr.app.R.string.filter_location_events_default_value))
                .equals(((EventsFragment) eventsFragmentView).getString(org.protestr.app.R.string.filter_location_events_default_value))) {
            iso3 = Prefs.with(((EventsFragment) eventsFragmentView)
                    .getContext())
                    .read(PreferencesUtils.PREFERENCES_SELECTED_COUNTRY, LocaleUtils.getDeviceLocale(((EventsFragment) eventsFragmentView).getContext()));
        }

        eventsInteractor.getNewEvents(this, iso3, offset, limit, order, lat, lng, userId);
    }

    @Override
    public void onNewEventsReceived(ArrayList<Event> newEvents) {
        eventsFragmentView.populateNewEventList(newEvents);
    }

    @Override
    public void onNewEventsError() {
        eventsFragmentView.showError();
    }

    @Override
    public void onDestroy() {
        eventsFragmentView = null;
    }
}
