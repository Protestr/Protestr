package org.protestr.app.ui.activities.event_info.image_viewer;

import android.graphics.Bitmap;

/**
 * Created by someone on 7/09/17.
 */

public interface ImageViewerPresenter {
    void storeImageOnDevice(Bitmap bitmap);

    void onDestroy();
}
