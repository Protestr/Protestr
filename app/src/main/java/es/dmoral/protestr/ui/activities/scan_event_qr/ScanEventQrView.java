package es.dmoral.protestr.ui.activities.scan_event_qr;

import es.dmoral.protestr.data.models.Event;

/**
 * Created by grender on 30/10/17.
 */

public interface ScanEventQrView {
    void onEventReceived(Event event);

    void onEventError();

    void prepareScanner();
}
