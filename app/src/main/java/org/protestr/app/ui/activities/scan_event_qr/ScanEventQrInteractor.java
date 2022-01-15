package org.protestr.app.ui.activities.scan_event_qr;

import org.protestr.app.data.models.dao.Event;

import org.protestr.app.data.models.dao.Event;

/**
 * Created by someone on 30/10/17.
 */

public interface ScanEventQrInteractor {
    interface OnGetEventListener {
        void eventReceived(Event event);

        void eventReceivedError();
    }

    void getEventById(OnGetEventListener onGetEventListener, String eventId, String userId);
}
