package es.dmoral.protestr.splash;

/**
 * Created by grender on 15/02/17.
 */

public interface SplashPresenter {

    void confirmLoginAttempt(final String username, final String password);
    void onDestroy();
}
