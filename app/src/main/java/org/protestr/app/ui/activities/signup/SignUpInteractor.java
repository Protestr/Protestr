package org.protestr.app.ui.activities.signup;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 1/07/17.
 */

public interface SignUpInteractor {
    interface OnAttemptSignUpListener {
        void onSignUpSuccess(User user);

        void onSignUpError(boolean isFailure);
    }

    void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username,
                       @NonNull final String email, @NonNull final String password, @NonNull final String profilePicUrl);
}
