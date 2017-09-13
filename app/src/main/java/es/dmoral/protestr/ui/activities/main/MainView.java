package es.dmoral.protestr.ui.activities.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by grender on 15/02/17.
 */

interface MainView {

    void addFragment(@NonNull Fragment newFragment);
    void showFab();
    void hideFab();
    void createEventAction();
    void enableLocation();
    void preLoadGoogleMaps();
}
