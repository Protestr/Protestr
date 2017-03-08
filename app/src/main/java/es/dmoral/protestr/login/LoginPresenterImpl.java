package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

import es.dmoral.protestr.R;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.InternetUtils;
import es.dmoral.prefs.Prefs;

/**
 * Created by grender on 13/02/17.
 */

class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnAttemptLoginListener, LoginInteractor.OnAttemptSignUpListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;

    LoginPresenterImpl(@NonNull LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void attemptLogin(@NonNull String username, @NonNull String password) {
        if (InternetUtils.isInternetAvailable((LoginActivity) loginView)) {
            loginView.showProgress();
            loginInteractor.attemptLogin(this, username, password);
        } else {
            loginView.connectionError();
        }
    }

    @Override
    public void attemptSignUp(@NonNull String username, @NonNull String password) {
        if (InternetUtils.isInternetAvailable((LoginActivity) loginView)) {
            loginView.showProgress();
            loginInteractor.attemptSignUp(this, username, password);
        } else {
            loginView.connectionError();
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onLoginSuccess(final String username, final String password) {
        Prefs.with((LoginActivity) loginView).writeBoolean(Constants.PREFERENCES_LOGGED_IN, true);
        Prefs.with((LoginActivity) loginView).write(Constants.PREFERENCES_USERNAME, username);
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

    @Override
    public void onSignUpSuccess(final String username, final String password) {
        onLoginSuccess(username, password);
    }

    @Override
    public void onSignUpError(boolean isFailure) {
        loginView.hideProgress();
        if (isFailure)
            loginView.connectionError();
        else
            loginView.signUpError(((LoginActivity) loginView).getString(R.string.username_not_valid_error));
    }
}
