package es.dmoral.protestr.fragments.events;

import java.util.ArrayList;

import es.dmoral.protestr.api.WebService;
import es.dmoral.protestr.api.models.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 16/02/17.
 */

public class EventsInteractorImpl implements EventsInteractor {
    @Override
    public void getNewEvents(final OnGetNewEventsListener onGetNewEventsListener) {
        new WebService().getApiInterface().getNewEvents().enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                onGetNewEventsListener.onNewEventsReceived(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                onGetNewEventsListener.onNewEventsError();
            }
        });
    }
}
