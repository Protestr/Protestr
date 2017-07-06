package es.dmoral.protestr.ui.activities.create_event;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.User;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.ImageUtils;
import es.dmoral.protestr.utils.PreferencesUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by grender on 17/06/17.
 */

class CreateEventPresenterImpl implements CreateEventPresenter, CreateEventInteractor.OnEventCreatedListener {
    private CreateEventView createEventView;
    private CreateEventInteractor createEventInteractor;

    CreateEventPresenterImpl(@NonNull CreateEventView createEventView) {
        this.createEventView = createEventView;
        createEventInteractor = new CreateEventInteractorImpl();
    }

    @Override
    public void createEvent(Bitmap bitmap, final String eventName, final String eventDescription, final long eventTime,
                            final String locationName, final double latitude, final double longitude, final String iso3) {
        final RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"),
                ImageUtils.bitmapToByteArray(bitmap));
        createEventInteractor.uploadImage(new CreateEventInteractor.OnImageUploadedListener() {
            @Override
            public void onImageUploaded(String imageUrl) {
                final User user = PreferencesUtils.getLoggedUser(((CreateEventActivity) createEventView));
                createEventInteractor.createEvent(CreateEventPresenterImpl.this, user.getId(), user.getPassword(), imageUrl, eventName, eventDescription,
                        String.valueOf(eventTime), locationName, String.valueOf(latitude), String.valueOf(longitude),
                        iso3);
            }

            @Override
            public void onImageError() {
                createEventView.hideProgress();
                createEventView.showError(((CreateEventActivity) createEventView).getString(R.string.error_uploading_image));
            }
        }, Constants.getImgurAuthHeader(((CreateEventActivity) createEventView)), imageBody);
    }

    @Override
    public void onEventCreated() {
        createEventView.hideProgress();
        createEventView.onEventCreated();
    }

    @Override
    public void onEventError() {
        createEventView.hideProgress();
        createEventView.showError(((CreateEventActivity) createEventView).getString(R.string.error_creating_event));
    }

    @Override
    public void onDestroy() {
        createEventView = null;
    }
}