package es.dmoral.protestr.fragments.events;

import java.util.ArrayList;

import es.dmoral.protestr.api.models.Event;

/**
 * Created by grender on 16/02/17.
 */

public interface EventsFragmentView {
    void populateNewEventList(ArrayList<Event> newEvents);
}
