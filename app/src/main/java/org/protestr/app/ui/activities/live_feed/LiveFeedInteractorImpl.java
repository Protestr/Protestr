package org.protestr.app.ui.activities.live_feed;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.ResponseStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveFeedInteractorImpl implements LiveFeedInteractor {
    @Override
    public void postUpdate(final OnUpdatePostedListener onUpdatePostedListener, String userEmail,
                           String password, String eventId, String eventName, String message,
                           long timestamp) {
        WebService.getInstance().getApiInterface().postUpdate(userEmail, password, eventId,
                eventName, message, timestamp).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                onUpdatePostedListener.onUpdatePosted();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onUpdatePostedListener.onUpdatePostedError();
            }
        });
    }
}
