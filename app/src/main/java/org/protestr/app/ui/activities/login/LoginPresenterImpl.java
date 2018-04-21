package org.protestr.app.ui.activities.login;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.InternetUtils;
import org.protestr.app.utils.PreferencesUtils;

import es.dmoral.prefs.Prefs;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.InternetUtils;
import org.protestr.app.utils.PreferencesUtils;

/**
 * Created by someone on 13/02/17.
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
    public void onLoginSuccess(User user) {
        Prefs.with((LoginActivity) loginView).writeBoolean(PreferencesUtils.PREFERENCES_LOGGED_IN, true);
        PreferencesUtils.storeLoggedUser((LoginActivity) loginView, user);
        loginView.hideProgress();
        loginView.loginSuccess();
    }

    @Override
    public void onLoginError(boolean isFailure) {
        loginView.hideProgress();
        if (isFailure)
            loginView.connectionError();
        else
            loginView.loginError(((LoginActivity) loginView).getString(org.protestr.app.R.string.invalid_credentials));
    }
}
