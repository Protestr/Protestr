package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

/**
 * Created by grender on 13/02/17.
 */

interface LoginInteractor {

    interface OnAttemptLoginListener {
        void onLoginSuccess(final String username, final String password);
        void onLoginError(boolean isFailure);
    }

    interface OnAttemptSignUpListener {
        void onSignUpSuccess(final String username, final String password);
        void onSignUpError(boolean isFailure);
    }

    void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String username, @NonNull final String password);
    void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username, @NonNull final String password);
}
