package org.protestr.app.ui.activities.event_info;

import android.graphics.Bitmap;
import android.support.annotation.StringRes;

/**
 * Created by someone on 7/09/17.
 */

public interface EventInfoView {
    void moveCameraBackToLocation();

    void moveCameraBackToLocation(boolean animate);

    void imageClicked();

    void openImageViewer(Bitmap bitmap);

    void showProgress(@StringRes int message);

    void hideProgress();

    void join();

    void onEventJoined();

    void leave();

    void onEventLeft();

    void delete();

    void onEventDeleted();

    void openLiveFeed();

    void generateQr();
}
