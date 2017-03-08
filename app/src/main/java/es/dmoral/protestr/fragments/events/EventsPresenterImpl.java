package es.dmoral.protestr.fragments.events;

import java.util.ArrayList;

import es.dmoral.protestr.api.models.Event;

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
    public void getNewEvents() {
        eventsInteractor.getNewEvents(this);
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
