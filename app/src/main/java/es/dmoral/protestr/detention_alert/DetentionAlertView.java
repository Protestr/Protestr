package es.dmoral.protestr.detention_alert;

/**
 * Created by grender on 5/06/17.
 */

public interface DetentionAlertView {
    void pickContact();
    void contactSelected(String displayName);
    void contactError();
    void showOngoingNotification();
    void clearNotification();
    void setNotificationState();
    void setButtonState();
    void setContactName();
}
