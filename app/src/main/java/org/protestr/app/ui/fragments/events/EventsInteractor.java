package org.protestr.app.ui.fragments.events;

import android.support.annotation.Nullable;

import org.protestr.app.data.models.dao.Event;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.Event;

/**
 * Created by someone on 16/02/17.
 */

public interface EventsInteractor {

    interface OnGetNewEventsListener {
        void onNewEventsReceived(ArrayList<Event> newEvents);

        void onNewEventsError();
    }

    void getNewEvents(final OnGetNewEventsListener onGetNewEventsListener, @Nullable String iso3Code,
                      int offset, int limit, String order, double lat, double lng, String userId);
}
