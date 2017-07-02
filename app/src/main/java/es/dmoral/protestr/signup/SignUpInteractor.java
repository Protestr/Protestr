package es.dmoral.protestr.signup;

import android.support.annotation.NonNull;

/**
 * Created by grender on 1/07/17.
 */

public interface SignUpInteractor {
    interface OnAttemptSignUpListener {
        void onSignUpSuccess(final String email, final String password);
        void onSignUpError(boolean isFailure);
    }

    void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username,
                       @NonNull final String email, @NonNull final String password);
}
