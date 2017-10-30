package es.dmoral.protestr.ui.activities.signup;

import android.support.annotation.NonNull;

/**
 * Created by grender on 1/07/17.
 */

public interface SignUpPresenter {
    void attemptSignUp(@NonNull String username, @NonNull final String email, @NonNull final String password, @NonNull final String profilePicUrl);

    void onDestroy();
}
