package org.protestr.app.ui.fragments.subscribed_events;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.Event;

public interface SubscribedEventsView {
    void populateSubscribedEvents(ArrayList<Event> subscribedEvents);

    void showError();
}
