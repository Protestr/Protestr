package es.dmoral.protestr.ui.activities.event_info.image_viewer;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * Created by grender on 7/09/17.
 */

public class ImageViewerPresenterImpl implements ImageViewerPresenter, ImageViewerInteractor.OnImageSavedListener {

    private ImageViewerView imageViewerView;
    private ImageViewerInteractor imageViewerInteractor;

    public ImageViewerPresenterImpl(ImageViewerView imageViewerView) {
        this.imageViewerView = imageViewerView;
        imageViewerInteractor = new ImageViewerInteractorImpl();
    }

    @Override
    public void storeImageOnDevice(Bitmap bitmap) {
        final File folder = new File(Environment.getExternalStorageDirectory() +
                "/Download");
        final String fileName = ((AppCompatActivity) imageViewerView).getString(
                ((AppCompatActivity) imageViewerView).getApplicationInfo().labelRes) + "-" +
                System.currentTimeMillis() + ".png";
        imageViewerInteractor.storeImage(this, folder, fileName, bitmap);
    }

    @Override
    public void onDestroy() {
        imageViewerView = null;
    }

    @Override
    public void onImageSaved(String filePath) {
        if (imageViewerView != null)
            imageViewerView.imageSaved(filePath);
    }

    @Override
    public void onImageError() {
        if (imageViewerView != null)
            imageViewerView.imageError();
    }
}
