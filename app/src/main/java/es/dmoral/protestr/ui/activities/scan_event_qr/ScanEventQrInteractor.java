package es.dmoral.protestr.ui.activities.scan_event_qr;

import es.dmoral.protestr.data.models.dao.Event;

/**
 * Created by grender on 30/10/17.
 */

public interface ScanEventQrInteractor {
    interface OnGetEventListener {
        void eventReceived(Event event);

        void eventReceivedError();
    }

    void getEventById(OnGetEventListener onGetEventListener, String eventId, String userId);
}
