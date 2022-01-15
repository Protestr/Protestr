package org.protestr.app.ui.activities.event_info;

public interface EventInfoInteractor {
    interface OnEventJoinedListener {
        void onEventJoined(String eventId);

        void onEventJoinedError();
    }

    interface OnEventLeftListener {
        void onEventLeft(String eventId);

        void onEventLeftError();
    }

    interface OnEventDeletedListener {
        void onEventDeleted(String eventId);

        void onEventDeletedError();
    }

    void joinEvent(OnEventJoinedListener onEventJoinedListener, String userEmail, String userPassword,
                   String eventId);

    void leaveEvent(OnEventLeftListener onEventLeftListener, String userEmail, String userPassword,
                    String eventId, boolean isAdmin);

    void deleteEvent(OnEventDeletedListener onEventDeletedListener, String userEmail, String userPassword,
                     String eventId);
}
