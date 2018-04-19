package es.dmoral.protestr.ui.fragments.subscribed_events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import es.dmoral.protestr.data.models.dao.Event;

public class SubscribedEventsPresenterImpl implements SubscribedEventsPresenter, SubscribedEventsInteractor.OnGetSubscribedEventsListener {

    private SubscribedEventsView subscribedEventsView;
    private SubscribedEventsInteractor subscribedEventsInteractor;

    SubscribedEventsPresenterImpl(SubscribedEventsView subscribedEventsView) {
        this.subscribedEventsView = subscribedEventsView;
        subscribedEventsInteractor = new SubscribedEventsInteractorImpl();
    }

    @Override
    public void getSubscribedEvents(String userEmail, String userPassword) {
        subscribedEventsInteractor.getSubscribedEvents(this, userEmail, userPassword);
    }

    @Override
    public void onSubscribedEventsReceived(ArrayList<Event> subscribedEvents) {
        Collections.sort(subscribedEvents, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event event2) {
                // Ascending
                return (int) (event.getFromDate() - event2.getFromDate());
            }
        });
        subscribedEventsView.populateSubscribedEvents(subscribedEvents);
    }

    @Override
    public void onSubscribedEventsError() {
        subscribedEventsView.showError();
    }

    @Override
    public void onDestroy() {
        subscribedEventsView = null;
    }
}
