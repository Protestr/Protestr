package es.dmoral.protestr.ui.activities.event_info.image_viewer;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by grender on 7/09/17.
 */

public interface ImageViewerInteractor {
    interface OnImageSavedListener {
        void onImageSaved(String filePath);
        void onImageError();
    }

    void storeImage(OnImageSavedListener onImageSavedListeners, File folder, String fileName, Bitmap bitmap);
}
