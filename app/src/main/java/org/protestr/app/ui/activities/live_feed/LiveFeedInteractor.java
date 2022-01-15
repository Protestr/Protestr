package org.protestr.app.ui.activities.live_feed;

public interface LiveFeedInteractor {
    interface OnUpdatePostedListener {
        void onUpdatePosted();

        void onUpdatePostedError();
    }

    void postUpdate(OnUpdatePostedListener onUpdatePostedListener, String userEmail, String password,
                    String eventId, String eventName, String message, long timestamp);
}
