package es.dmoral.protestr.create_event;

import es.dmoral.protestr.api.WebService;
import es.dmoral.protestr.models.models.ImgurStatus;
import es.dmoral.protestr.models.models.ResponseStatus;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 17/06/17.
 */

class CreateEventInteractorImpl implements CreateEventInteractor {
    @Override
    public void uploadImage(final OnImageUploadedListener onImageUploadedListener, String clientId,
                            RequestBody imageBody) {
        new WebService().getApiInterface().uploadImage(clientId, imageBody).enqueue(new Callback<ImgurStatus>() {
            @Override
            public void onResponse(Call<ImgurStatus> call, Response<ImgurStatus> response) {
                onImageUploadedListener.onImageUploaded(response.body().getData().getLink());
            }

            @Override
            public void onFailure(Call<ImgurStatus> call, Throwable t) {
                onImageUploadedListener.onImageError();
            }
        });
    }

    @Override
    public void createEvent(final OnEventCreatedListener onEventCreatedListener,
                            String userName, String password, String imageUrl, String eventName,
                            String eventDescription, String eventTime,
                            String locationName, String latitude, String longitude, String iso3) {
        new WebService().getApiInterface().createNewEvent(userName, password, eventName,
                imageUrl, eventDescription, eventTime, locationName, latitude, longitude,
                iso3).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                onEventCreatedListener.onEventCreated();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onEventCreatedListener.onEventError();
            }
        });
    }
}
