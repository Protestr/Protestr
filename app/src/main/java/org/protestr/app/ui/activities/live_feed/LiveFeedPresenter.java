package org.protestr.app.ui.activities.live_feed;

public interface LiveFeedPresenter {
    void postUpdate(String userEmail, String password, String eventId, String eventName,
                    String message);

    void onDestroy();
}
