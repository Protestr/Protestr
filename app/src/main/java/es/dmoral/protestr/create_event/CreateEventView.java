package es.dmoral.protestr.create_event;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by grender on 16/06/17.
 */

public interface CreateEventView {
    void restoreStates(@Nullable Bundle savedInstanceState);
    void updateEventImage(@Nullable Bitmap bitmap);
    void updateTimeIfNeeded();
    void setDate(long timeInMillis);
    void setTime();
}
