package es.dmoral.protestr.signup;

import android.support.annotation.NonNull;

import es.dmoral.protestr.models.models.User;

/**
 * Created by grender on 1/07/17.
 */

public interface SignUpInteractor {
    interface OnAttemptSignUpListener {
        void onSignUpSuccess(User user);
        void onSignUpError(boolean isFailure);
    }

    void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username,
                       @NonNull final String email, @NonNull final String password, @NonNull final String profilePicUrl);
}
