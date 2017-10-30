package es.dmoral.protestr.ui.activities.event_info;

import android.graphics.Bitmap;

/**
 * Created by grender on 7/09/17.
 */

public interface EventInfoView {
    void moveCameraBackToLocation();

    void moveCameraBackToLocation(boolean animate);

    void imageClicked();

    void openImageViewer(Bitmap bitmap);

    void subscribe();

    void generateQr();
}
