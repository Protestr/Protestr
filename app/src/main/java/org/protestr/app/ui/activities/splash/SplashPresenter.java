package org.protestr.app.ui.activities.splash;

/**
 * Created by someone on 15/02/17.
 */

public interface SplashPresenter {

    void confirmLoginAttempt(final String email, final String password);

    void onDestroy();
}
