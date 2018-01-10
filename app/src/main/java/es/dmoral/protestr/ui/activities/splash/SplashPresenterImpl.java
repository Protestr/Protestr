package es.dmoral.protestr.ui.activities.splash;

import android.support.annotation.NonNull;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.data.models.User;
import es.dmoral.protestr.utils.PreferencesUtils;

/**
 * Created by grender on 15/02/17.
 */

public class SplashPresenterImpl implements SplashPresenter, SplashInteractor.OnConfirmLoginListener {

    private SplashView splashView;
    private SplashInteractor splashInteractor;

    SplashPresenterImpl(@NonNull SplashView splashView) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }

    @Override
    public void confirmLoginAttempt(final String email, final String password) {
        splashInteractor.confirmLogin(this, email, password);
    }

    @Override
    public void onLoginConfirmed(User user) {
        Prefs.with((SplashActivity) splashView).writeBoolean(PreferencesUtils.PREFERENCES_LOGGED_IN, true);
        PreferencesUtils.storeLoggedUser((SplashActivity) splashView, user);
        if (splashView != null) {
            splashView.loginConfirmed();
        } else {
            confirmLoginAttempt(user.getEmail(), user.getPassword());
        }
    }

    @Override
    public void onLoginUnconfirmed() {
        if (splashView != null)
            splashView.loginUnconfirmed();
    }

    @Override
    public void onDestroy() {
        this.splashView = null;
    }
}
