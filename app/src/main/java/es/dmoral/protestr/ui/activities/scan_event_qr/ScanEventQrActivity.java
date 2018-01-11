package es.dmoral.protestr.ui.activities.scan_event_qr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.dao.Event;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.activities.event_info.EventInfoActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.toasty.Toasty;

public class ScanEventQrActivity extends BaseActivity implements ScanEventQrView {
    @BindView(R.id.capture_view)
    DecoratedBarcodeView captureView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean retrieving;
    private Vibrator vibrator;
    private ScanEventQrPresenter scanEventQrPresenter;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_scan_event_qr);

        scanEventQrPresenter = new ScanEventQrPresenterImpl(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        prepareScanner();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toasty.warning(ScanEventQrActivity.this, getString(R.string.camera_needed)).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onEventReceived(Event event) {
        retrieving = false;
        final Intent eventInfoIntent = new Intent(this, EventInfoActivity.class);
        eventInfoIntent.putExtra(Constants.EVENT_INFO_EXTRA, event);
        startActivity(eventInfoIntent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public void onEventError() {
        retrieving = false;
        Toasty.error(this, getString(R.string.invalid_qr_event)).show();
    }

    @Override
    public void prepareScanner() {
        captureView.decodeContinuous(new BarcodeCallback() {
            private static final long DELAY_BETWEEN_SCANS = 2000;
            private long lastTimestamp = 0;

            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!retrieving) {
                    if (result.getText() != null) {
                        if (System.currentTimeMillis() - lastTimestamp < DELAY_BETWEEN_SCANS) {
                            return;
                        }
                        lastTimestamp = System.currentTimeMillis();
                        vibrator.vibrate(50);
                        Toasty.info(ScanEventQrActivity.this, getString(R.string.loading_event)).show();
                        scanEventQrPresenter.getEventById(result.getText());
                        retrieving = true;
                    }
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // unused
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return captureView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        captureView.setStatusText(null);
    }

    @Override
    protected void setListeners() {
        // unused
    }
}
