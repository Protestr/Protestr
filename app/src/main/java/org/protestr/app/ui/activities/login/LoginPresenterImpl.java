package org.protestr.app.ui.activities.login;

import android.support.annotation.NonNull;

import org.protestr.app.data.fcm.FCMHelper;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.InternetUtils;
import org.protestr.app.utils.PreferencesUtils;

import es.dmoral.prefs.Prefs;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.InternetUtils;
import org.protestr.app.utils.PreferencesUtils;

import java.util.ArrayList;

/**
 * Created by someone on 13/02/17.
 */

class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnAttemptLoginListener, LoginInteractor.OnSubscribedEventsReceivedListener {

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
    public void resubscribeToEventsIfNeeded(User user) {
        if (Prefs.with((LoginActivity) loginView).readBoolean(PreferencesUtils.PREFERENCES_NEEDS_EVENTS_SUBSCRIBTION, true)) {
            loginInteractor.getSubscribedEvents(this, user.getEmail(), user.getPassword());
        } else {
            loginView.hideProgress();
            loginView.loginSuccess();
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
        resubscribeToEventsIfNeeded(user);
    }

    @Override
    public void onLoginError(boolean isFailure) {
        loginView.hideProgress();
        if (isFailure)
            loginView.connectionError();
        else
            loginView.loginError(((LoginActivity) loginView).getString(org.protestr.app.R.string.invalid_credentials));
    }

    @Override
    public void onSubscribedEventsReceived(ArrayList<Event> events) {
        ArrayList<String> eventIds = new ArrayList<>();
        for (Event event : events) {
            eventIds.add(event.getEventId());
        }
        FCMHelper.subscribeToEvents(eventIds);
        Prefs.with((LoginActivity) loginView).writeBoolean(PreferencesUtils.PREFERENCES_NEEDS_EVENTS_SUBSCRIBTION, false);
        loginView.hideProgress();
        loginView.loginSuccess();
    }

    @Override
    public void onSubscribedEventsReceivedError() {
        Prefs.with((LoginActivity) loginView).writeBoolean(PreferencesUtils.PREFERENCES_NEEDS_EVENTS_SUBSCRIBTION, true);
    }
}
