package es.dmoral.protestr.ui.fragments.subscribed_events;

public interface SubscribedEventsPresenter {
    void getSubscribedEvents(String userEmail, String userPassword);

    void onDestroy();
}
