package org.protestr.app.ui.activities.event_info;

import android.support.annotation.NonNull;

public class EventInfoPresenterImpl implements EventInfoPresenter, EventInfoInteractor.OnEventJoinedListener,
        EventInfoInteractor.OnEventLeftListener, EventInfoInteractor.OnEventDeletedListener {
    private EventInfoView eventInfoView;
    private EventInfoInteractor eventInfoInteractor;

    EventInfoPresenterImpl(@NonNull EventInfoView eventInfoView) {
        this.eventInfoView = eventInfoView;
        eventInfoInteractor = new EventInfoInteractorImpl();
    }

    @Override
    public void joinEvent(String userEmail, String userPassword, String eventId) {
        eventInfoInteractor.joinEvent(this, userEmail, userPassword, eventId);
    }

    @Override
    public void leaveEvent(String userEmail, String userPassword, String eventId, boolean isAdmin) {
        eventInfoInteractor.leaveEvent(this, userEmail, userPassword, eventId, isAdmin);
    }

    @Override
    public void deleteEvent(String userEmail, String userPassword, String eventId) {
        eventInfoInteractor.deleteEvent(this, userEmail, userPassword, eventId);
    }

    @Override
    public void onEventJoined() {
        eventInfoView.hideProgress();
        eventInfoView.onEventJoined();
    }

    @Override
    public void onEventJoinedError() {
        eventInfoView.hideProgress();
    }

    @Override
    public void onEventLeft() {
        eventInfoView.hideProgress();
        eventInfoView.onEventLeft();
    }

    @Override
    public void onEventLeftError() {
        eventInfoView.hideProgress();
    }

    @Override
    public void onEventDeleted() {
        eventInfoView.hideProgress();
        eventInfoView.onEventDeleted();
    }

    @Override
    public void onEventDeletedError() {
        eventInfoView.hideProgress();
    }

    @Override
    public void onDestroy() {
        eventInfoView = null;
    }
}
