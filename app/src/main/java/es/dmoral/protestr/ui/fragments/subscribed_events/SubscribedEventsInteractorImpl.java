package es.dmoral.protestr.ui.fragments.subscribed_events;

import java.util.ArrayList;

import es.dmoral.protestr.data.api.WebService;
import es.dmoral.protestr.data.models.dao.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribedEventsInteractorImpl implements SubscribedEventsInteractor {
    @Override
    public void getSubscribedEvents(final OnGetSubscribedEventsListener onGetSubscribedEventsListener, String userEmail, String userPassword) {
        WebService.getInstance().getApiInterface().getSubscribedEvents(userEmail, userPassword).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                onGetSubscribedEventsListener.onSubscribedEventsReceived(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                onGetSubscribedEventsListener.onSubscribedEventsError();
            }
        });
    }
}
