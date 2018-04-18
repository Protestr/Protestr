package es.dmoral.protestr.ui.activities.event_info;

import es.dmoral.protestr.data.api.WebService;
import es.dmoral.protestr.data.models.dao.ResponseStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoInteractorImpl implements EventInfoInteractor {
    @Override
    public void joinEvent(final OnEventJoinedListener onEventJoinedListener, String userEmail, String userPassword, String eventId) {
        WebService.getInstance().getApiInterface().joinEvent(userEmail, userPassword, eventId).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                onEventJoinedListener.onEventJoined();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onEventJoinedListener.onEventJoinedError();
            }
        });
    }

    @Override
    public void leaveEvent(final OnEventLeftListener onEventLeftListener, String userEmail, String userPassword, String eventId, boolean isAdmin) {
        WebService.getInstance().getApiInterface().leaveEvent(userEmail, userPassword, eventId, isAdmin).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                onEventLeftListener.onEventLeft();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onEventLeftListener.onEventLeftError();
            }
        });
    }

    @Override
    public void deleteEvent(final OnEventDeletedListener onEventDeletedListener, String userEmail, String userPassword, String eventId) {
        WebService.getInstance().getApiInterface().deleteEvent(userEmail, userPassword, eventId).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                onEventDeletedListener.onEventDeleted();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onEventDeletedListener.onEventDeletedError();
            }
        });
    }
}
