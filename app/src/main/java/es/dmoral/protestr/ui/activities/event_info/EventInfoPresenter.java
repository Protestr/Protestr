package es.dmoral.protestr.ui.activities.event_info;

public interface EventInfoPresenter {
    void joinEvent(String userEmail, String userPassword, String eventId);

    void leaveEvent(String userEmail, String userPassword, String eventId, boolean isAdmin);

    void deleteEvent(String userEmail, String userPassword, String eventId);

    void onDestroy();
}
