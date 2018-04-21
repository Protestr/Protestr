package org.protestr.app.ui.activities.login;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 13/02/17.
 */

interface LoginInteractor {

    interface OnAttemptLoginListener {
        void onLoginSuccess(User user);

        void onLoginError(boolean isFailure);
    }

    void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String email,
                      @NonNull final String password);
}
