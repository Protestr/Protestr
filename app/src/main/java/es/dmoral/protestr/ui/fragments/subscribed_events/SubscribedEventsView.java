package es.dmoral.protestr.ui.fragments.subscribed_events;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.Event;

public interface SubscribedEventsView {
    void populateSubscribedEvents(ArrayList<Event> subscribedEvents);

    void showError();
}
