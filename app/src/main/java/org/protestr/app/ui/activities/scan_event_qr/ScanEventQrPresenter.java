package org.protestr.app.ui.activities.scan_event_qr;

/**
 * Created by someone on 30/10/17.
 */

public interface ScanEventQrPresenter {
    void getEventById(String eventId);

    void onDestroy();
}
