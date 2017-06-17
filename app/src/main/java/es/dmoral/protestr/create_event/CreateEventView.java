package es.dmoral.protestr.create_event;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by grender on 16/06/17.
 */

public interface CreateEventView {
    void restoreStates(@Nullable Bundle savedInstanceState);
    void updateEventImage(@Nullable Bitmap bitmap);
    void updateTimeIfNeeded();
    void moveMapCamera(LatLng latLng);
    void setDate(long timeInMillis);
    void setTime();
}
