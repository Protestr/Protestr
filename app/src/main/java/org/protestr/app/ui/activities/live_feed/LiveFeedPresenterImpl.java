package org.protestr.app.ui.activities.live_feed;

import android.support.annotation.NonNull;

import org.protestr.app.utils.TimeUtils;

public class LiveFeedPresenterImpl implements LiveFeedPresenter, LiveFeedInteractor.OnUpdatePostedListener {
    private LiveFeedView liveFeedView;
    private LiveFeedInteractor liveFeedInteractor;

    public LiveFeedPresenterImpl(@NonNull LiveFeedView liveFeedView) {
        this.liveFeedView = liveFeedView;
        liveFeedInteractor = new LiveFeedInteractorImpl();
    }

    @Override
    public void postUpdate(String userEmail, String password, String eventId, String eventName, String message) {
        liveFeedInteractor.postUpdate(this, userEmail, password, eventId,
                eventName, message, TimeUtils.getCurrentTimeInMillis());
    }

    @Override
    public void onDestroy() {
        liveFeedView = null;
    }

    @Override
    public void onUpdatePosted() {
        // unused
    }

    @Override
    public void onUpdatePostedError() {
        // unused
    }
}
