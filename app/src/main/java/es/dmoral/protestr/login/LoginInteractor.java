package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

/**
 * Created by grender on 13/02/17.
 */

interface LoginInteractor {

    interface OnAttemptLoginListener {
        void onLoginSuccess(final String email, final String password);
        void onLoginError(boolean isFailure);
    }

    void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String email,
                      @NonNull final String password);
}
