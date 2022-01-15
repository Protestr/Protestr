package org.protestr.app.ui.activities.scan_event_qr;

import org.protestr.app.data.models.dao.Event;

import org.protestr.app.data.models.dao.Event;

/**
 * Created by someone on 30/10/17.
 */

public interface ScanEventQrView {
    void onEventReceived(Event event);

    void onEventError();

    void prepareScanner();
}
