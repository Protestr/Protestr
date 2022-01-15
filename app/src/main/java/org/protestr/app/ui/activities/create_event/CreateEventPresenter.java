package org.protestr.app.ui.activities.create_event;

import android.graphics.Bitmap;

import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 17/06/17.
 */

interface CreateEventPresenter {
    void createEvent(Bitmap bitmap, String eventName, String eventDescription, long eventTime,
                     String locationName, double latitude, double longitude, String iso3,
                     ArrayList<User> eventAdmins);

    void onDestroy();
}
