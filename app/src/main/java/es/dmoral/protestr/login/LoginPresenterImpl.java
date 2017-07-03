package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.InternetUtils;

/**
 * Created by grender on 13/02/17.
 */

class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnAttemptLoginListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;

    LoginPresenterImpl(@NonNull LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void attemptLogin(@NonNull String email, @NonNull String password) {
        if (InternetUtils.isInternetAvailable((LoginActivity) loginView)) {
            loginView.showProgress();
            loginInteractor.attemptLogin(this, email, password);
        } else {
            loginView.connectionError();
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onLoginSuccess(final String email, final String password) {
        Prefs.with((LoginActivity) loginView).writeBoolean(Constants.PREFERENCES_LOGGED_IN, true);
        Prefs.with((LoginActivity) loginView).write(Constants.PREFERENCES_EMAIL, email);
        Prefs.with((LoginActivity) loginView).write(Constants.PREFERENCES_PASSWORD, password);
        loginView.hideProgress();
        loginView.loginSuccess();
    }

    @Override
    public void onLoginError(boolean isFailure) {
        loginView.hideProgress();
        if (isFailure)
            loginView.connectionError();
        else
            loginView.loginError(((LoginActivity) loginView).getString(R.string.invalid_credentials));
    }
}
