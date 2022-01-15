package org.protestr.app.ui.activities.create_event;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.ImgurStatus;
import org.protestr.app.data.models.dao.ResponseStatus;

import java.util.ArrayList;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.ImgurStatus;
import org.protestr.app.data.models.dao.ResponseStatus;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by someone on 17/06/17.
 */

class CreateEventInteractorImpl implements CreateEventInteractor {
    @Override
    public void uploadImage(final OnImageUploadedListener onImageUploadedListener, String clientId,
                            RequestBody imageBody) {
        WebService.getInstance().getApiInterface().uploadImage(clientId, imageBody).enqueue(new Callback<ImgurStatus>() {
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
                            String userId, String password, String imageUrl, String eventName,
                            String eventDescription, String eventTime,
                            String locationName, String latitude, String longitude, String iso3,
                            int participants, ArrayList<String> eventAdmins) {
        WebService.getInstance().getApiInterface().createNewEvent(userId, password, eventName,
                imageUrl, eventDescription, eventTime, locationName, latitude, longitude,
                iso3, participants, eventAdmins).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.isSuccessful())
                    onEventCreatedListener.onEventCreated();
                else
                    onEventCreatedListener.onEventError();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onEventCreatedListener.onEventError();
            }
        });
    }
}
