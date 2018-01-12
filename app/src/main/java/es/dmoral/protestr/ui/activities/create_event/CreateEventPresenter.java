package es.dmoral.protestr.ui.activities.create_event;

import android.graphics.Bitmap;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.User;

/**
 * Created by grender on 17/06/17.
 */

interface CreateEventPresenter {
    void createEvent(Bitmap bitmap, String eventName, String eventDescription, long eventTime,
                     String locationName, double latitude, double longitude, String iso3,
                     ArrayList<User> eventAdmins);

    void onDestroy();
}
