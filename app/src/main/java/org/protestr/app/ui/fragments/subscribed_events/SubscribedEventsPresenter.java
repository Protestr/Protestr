package org.protestr.app.ui.fragments.subscribed_events;

public interface SubscribedEventsPresenter {
    void getSubscribedEvents(String userEmail, String userPassword);

    void onDestroy();
}
