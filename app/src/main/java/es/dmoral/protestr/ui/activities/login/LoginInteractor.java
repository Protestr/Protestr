package es.dmoral.protestr.ui.activities.login;

import android.support.annotation.NonNull;

import es.dmoral.protestr.data.models.User;

/**
 * Created by grender on 13/02/17.
 */

interface LoginInteractor {

    interface OnAttemptLoginListener {
        void onLoginSuccess(User user);
        void onLoginError(boolean isFailure);
    }

    void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String email,
                      @NonNull final String password);
}
