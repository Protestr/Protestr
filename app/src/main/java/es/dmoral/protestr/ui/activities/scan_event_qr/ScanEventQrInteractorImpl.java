package es.dmoral.protestr.ui.activities.scan_event_qr;

import es.dmoral.protestr.data.api.WebService;
import es.dmoral.protestr.data.models.dao.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 30/10/17.
 */

public class ScanEventQrInteractorImpl implements ScanEventQrInteractor {
    @Override
    public void getEventById(final OnGetEventListener onGetEventListener, final String eventId) {
        WebService.getInstance().getApiInterface().getEventById(eventId).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful())
                    onGetEventListener.eventReceived(response.body());
                else
                    onGetEventListener.eventReceivedError();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                onGetEventListener.eventReceivedError();
            }
        });
    }
}
