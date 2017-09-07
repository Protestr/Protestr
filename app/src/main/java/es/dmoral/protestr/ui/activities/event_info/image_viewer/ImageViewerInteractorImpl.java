package es.dmoral.protestr.ui.activities.event_info.image_viewer;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.protestr.R;
import es.dmoral.toasty.Toasty;

/**
 * Created by grender on 7/09/17.
 */

public class ImageViewerInteractorImpl implements ImageViewerInteractor {
    @Override
    public void storeImage(final OnImageSavedListener onImageSavedListeners, final File folder,
                           final String fileName, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                final String fullPath = folder.getAbsolutePath() + "/" + fileName;
                FileOutputStream fileOutputStream = null;
                try {
                    if (!folder.exists())
                        if (!folder.mkdir())
                            error = true;
                    fileOutputStream = new FileOutputStream(fullPath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    error = true;
                } finally {
                    try {
                        if (fileOutputStream != null)
                            fileOutputStream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        error = true;
                    }
                }

                if (error)
                    onImageSavedListeners.onImageError();
                else
                    onImageSavedListeners.onImageSaved("file://" + fullPath);
            }
        }).start();
    }
}
