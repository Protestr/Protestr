package org.protestr.app.ui.activities.detention_alert;

/**
 * Created by someone on 5/06/17.
 */

interface DetentionAlertView {
    void contactSelected(String displayName);

    void contactError();

    void showOngoingNotification();

    void clearNotification();

    void setNotificationState();

    void setButtonState();

    void setMessage();

    void setContactName();

    void selectContactAction();

    void enableAlertAction();
}
