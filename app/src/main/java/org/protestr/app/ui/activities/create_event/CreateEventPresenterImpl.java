package org.protestr.app.ui.activities.create_event;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.ImageUtils;
import org.protestr.app.utils.PreferencesUtils;

import java.util.ArrayList;

import org.protestr.app.R;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.ImageUtils;
import org.protestr.app.utils.PreferencesUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by someone on 17/06/17.
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
                            final String locationName, final double latitude, final double longitude, final String iso3,
                            final ArrayList<User> eventAdmins) {
        final RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"),
                ImageUtils.bitmapToByteArray(bitmap));
        createEventInteractor.uploadImage(new CreateEventInteractor.OnImageUploadedListener() {
            @Override
            public void onImageUploaded(String imageUrl) {
                final User user = PreferencesUtils.getLoggedUser(((CreateEventActivity) createEventView));
                ArrayList<String> parsedEventAdmins = new ArrayList<>();
                for (User admin : eventAdmins) {
                    parsedEventAdmins.add(admin.getId());
                }
                createEventInteractor.createEvent(CreateEventPresenterImpl.this, user.getId(), user.getPassword(), imageUrl, eventName, eventDescription,
                        String.valueOf(eventTime), locationName, String.valueOf(latitude), String.valueOf(longitude),
                        iso3, parsedEventAdmins.size(), parsedEventAdmins);
            }

            @Override
            public void onImageError() {
                createEventView.hideProgress();
                createEventView.showError(((CreateEventActivity) createEventView).getString(org.protestr.app.R.string.error_uploading_image));
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
        createEventView.showError(((CreateEventActivity) createEventView).getString(org.protestr.app.R.string.error_creating_event));
    }

    @Override
    public void onDestroy() {
        createEventView = null;
    }
}
