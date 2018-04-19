package es.dmoral.protestr.ui.fragments.subscribed_events;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.Event;

public interface SubscribedEventsInteractor {

    interface OnGetSubscribedEventsListener {
        void onSubscribedEventsReceived(ArrayList<Event> subscribedEvents);

        void onSubscribedEventsError();
    }

    void getSubscribedEvents(OnGetSubscribedEventsListener onGetSubscribedEventsListener, String userEmail, String userPassword);
}
