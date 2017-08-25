package es.dmoral.protestr.ui.fragments.events;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.Event;

/**
 * Created by grender on 16/02/17.
 */

public interface EventsInteractor {

    interface OnGetNewEventsListener {
        void onNewEventsReceived(ArrayList<Event> newEvents);
        void onNewEventsError();
    }

    void getNewEvents(final OnGetNewEventsListener onGetNewEventsListener, @Nullable String iso3Code, int offset, int limit, String order, double lat, double lng);
}
