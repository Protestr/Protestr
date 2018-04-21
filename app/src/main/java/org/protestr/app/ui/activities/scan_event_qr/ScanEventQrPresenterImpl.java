package org.protestr.app.ui.activities.scan_event_qr;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.Event;
import org.protestr.app.utils.PreferencesUtils;

import org.protestr.app.data.models.dao.Event;
import org.protestr.app.utils.PreferencesUtils;

/**
 * Created by someone on 30/10/17.
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
        final String userId = PreferencesUtils.getLoggedUser((ScanEventQrActivity) scanEventQrView).getId();
        scanEventQrInteractor.getEventById(this, eventId, userId);
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
