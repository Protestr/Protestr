package es.dmoral.protestr.detention_alert;

/**
 * Created by grender on 5/06/17.
 */

public interface DetentionAlertView {
    void showOngoingNotification();
    void clearNotification();
    void setButtonState();
}
