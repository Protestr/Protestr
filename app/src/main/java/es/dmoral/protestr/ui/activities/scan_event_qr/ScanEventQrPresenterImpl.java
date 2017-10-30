package es.dmoral.protestr.ui.activities.scan_event_qr;

import android.support.annotation.NonNull;

import es.dmoral.protestr.data.models.Event;

/**
 * Created by grender on 30/10/17.
 */

public class ScanEventQrPresenterImpl implements ScanEventQrPresenter, ScanEventQrInteractor.OnGetEventListener {
    private ScanEventQrView scanEventQrView;
    private ScanEventQrInteractor scanEventQrInteractor;

    public ScanEventQrPresenterImpl(@NonNull ScanEventQrView scanEventQrView) {
        this.scanEventQrView = scanEventQrView;
        scanEventQrInteractor = new ScanEventQrInteractorImpl();
    }

    @Override
    public void getEventById(String eventId) {
        scanEventQrInteractor.getEventById(this, eventId);
    }

    @Override
    public void onDestroy() {
        scanEventQrView = null;
    }

    @Override
    public void eventReceived(Event event) {
        scanEventQrView.onEventReceived(event);
    }

    @Override
    public void eventReceivedError() {
        scanEventQrView.onEventError();
    }
}
