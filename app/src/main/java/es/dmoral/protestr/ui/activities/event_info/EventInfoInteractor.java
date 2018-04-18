package es.dmoral.protestr.ui.activities.event_info;

public interface EventInfoInteractor {
    interface OnEventJoinedListener {
        void onEventJoined();

        void onEventJoinedError();
    }

    interface OnEventLeftListener {
        void onEventLeft();

        void onEventLeftError();
    }

    interface OnEventDeletedListener {
        void onEventDeleted();

        void onEventDeletedError();
    }

    void joinEvent(OnEventJoinedListener onEventJoinedListener, String userEmail, String userPassword,
                   String eventId);

    void leaveEvent(OnEventLeftListener onEventLeftListener, String userEmail, String userPassword,
                    String eventId, boolean isAdmin);

    void deleteEvent(OnEventDeletedListener onEventDeletedListener, String userEmail, String userPassword,
                     String eventId);
}
