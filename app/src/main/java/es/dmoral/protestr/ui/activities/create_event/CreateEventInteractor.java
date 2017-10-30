package es.dmoral.protestr.ui.activities.create_event;

import okhttp3.RequestBody;

/**
 * Created by grender on 17/06/17.
 */

interface CreateEventInteractor {
    interface OnImageUploadedListener {
        void onImageUploaded(String imageUrl);

        void onImageError();
    }

    interface OnEventCreatedListener {
        void onEventCreated();

        void onEventError();
    }

    void uploadImage(OnImageUploadedListener onImageUploadedListener, String clientId, RequestBody imageBody);

    void createEvent(final OnEventCreatedListener onEventCreatedListener, String userId, String password,
                     String imageUrl, String eventName, String eventDescription, String eventTime,
                     String locationName, String latitude, String longitude, String iso3);
}
