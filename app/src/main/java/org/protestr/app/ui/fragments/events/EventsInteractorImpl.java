package org.protestr.app.ui.fragments.events;

import android.support.annotation.Nullable;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.Event;

import java.util.ArrayList;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by someone on 16/02/17.
 */

public class EventsInteractorImpl implements EventsInteractor {

    @Override
    public void getNewEvents(final OnGetNewEventsListener onGetNewEventsListener, @Nullable String iso3Code, int offset, int limit, String order,
                             double lat, double lng, String userId) {
        if (iso3Code == null)
            WebService.getInstance().getApiInterface().getAllNewEvents(offset, limit, order, lat, lng, userId).enqueue(new Callback<ArrayList<Event>>() {
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
            WebService.getInstance().getApiInterface().getNewEventsByIso3(iso3Code, offset, limit, order, userId).enqueue(new Callback<ArrayList<Event>>() {
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
