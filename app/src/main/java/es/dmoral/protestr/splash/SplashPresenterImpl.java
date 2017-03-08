package es.dmoral.protestr.splash;

import android.support.annotation.NonNull;

/**
 * Created by grender on 15/02/17.
 */

public class SplashPresenterImpl implements SplashPresenter, SplashInteractor.OnConfirmLoginListener {

    private SplashView splashView;
    private SplashInteractor splashInteractor;

    SplashPresenterImpl (@NonNull SplashView splashView) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }

    @Override
    public void confirmLoginAttempt(final String username, final String password) {
        splashInteractor.confirmLogin(this, username, password);
    }

    @Override
    public void onLoginConfirmed() {
        splashView.loginConfirmed();
    }

    @Override
    public void onLoginUnconfirmed() {
        splashView.loginUnconfirmed();
    }

    @Override
    public void onDestroy() {
        this.splashView = null;
    }
}
