package es.dmoral.protestr.ui.fragments.events;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import es.dmoral.protestr.data.api.WebService;
import es.dmoral.protestr.data.models.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 16/02/17.
 */

public class EventsInteractorImpl implements EventsInteractor {

    @Override
    public void getNewEvents(final OnGetNewEventsListener onGetNewEventsListener, @Nullable String iso3Code, int offset, int limit, String order) {
        if (iso3Code == null)
            WebService.getInstance().getApiInterface().getAllNewEvents(offset, limit, order).enqueue(new Callback<ArrayList<Event>>() {
                @Override
                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                    onGetNewEventsListener.onNewEventsReceived(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                    onGetNewEventsListener.onNewEventsError();
                }
            });
        else
            WebService.getInstance().getApiInterface().getNewEvents(iso3Code, offset, limit, order).enqueue(new Callback<ArrayList<Event>>() {
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
