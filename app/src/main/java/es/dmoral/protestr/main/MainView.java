package es.dmoral.protestr.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by grender on 15/02/17.
 */

interface MainView {
    void addFragment(boolean firstFragment, @NonNull Fragment newFragment);
    void showFab();
    void hideFab();
}
