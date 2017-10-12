package es.dmoral.protestr.ui.activities.event_info.image_viewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnLongClick;
import es.dmoral.protestr.R;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.activities.main.MainActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.toasty.Toasty;

public class ImageViewerActivity extends BaseActivity implements ImageViewerView {

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.photo_view) PhotoView photoView;

    private Bitmap currentBitmap;
    private ImageViewerPresenter imageViewerPresenter = new ImageViewerPresenterImpl(this);

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_image_viewer);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void setupViews() {
        registerForContextMenu(photoView);

        byte[] bitmapByteArray = getIntent().getByteArrayExtra(Constants.IMAGE_VIEWER_EXTRA);
        currentBitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
        photoView.setImageBitmap(currentBitmap);

        setImmersiveMode();
    }

    @Override
    protected void setListeners() {

    }

    private void saveImageToDisk() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        imageViewerPresenter.storeImageOnDevice(currentBitmap);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toasty.warning(ImageViewerActivity.this, getString(R.string.write_external_storage_cancelled_warning)).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
    }

    @Override
    public void imageSaved(final String filePath) {
        Snackbar.make(coordinatorLayout, getString(R.string.image_saved), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.view), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(filePath), "image/*");
                        startActivity(intent);
                    }
                }).setActionTextColor(ContextCompat.getColor(ImageViewerActivity.this, R.color.colorPrimary)).show();
    }

    @Override
    public void imageError() {
        Toasty.error(ImageViewerActivity.this, getString(R.string.error_ocurred_saving_image)).show();
    }

    @OnLongClick(R.id.photo_view)
    @Override
    public boolean openContextualMenu() {
        openContextMenu(photoView);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_viewer_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download_image:
                saveImageToDisk();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageViewerPresenter != null)
            imageViewerPresenter.onDestroy();
    }
}
