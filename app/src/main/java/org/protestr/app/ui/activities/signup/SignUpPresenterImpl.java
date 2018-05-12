package org.protestr.app.ui.activities.signup;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;
import org.protestr.app.utils.InternetUtils;
import org.protestr.app.utils.PreferencesUtils;

import es.dmoral.prefs.Prefs;

/**
 * Created by someone on 1/07/17.
 */

public class SignUpPresenterImpl implements SignUpPresenter, SignUpInteractor.OnAttemptSignUpListener {

    private SignUpView signUpView;
    private SignUpInteractor signUpInteractor;

    public SignUpPresenterImpl(@NonNull SignUpView signUpView) {
        this.signUpView = signUpView;
        signUpInteractor = new SignUpInteractorImpl();
    }

    @Override
    public void attemptSignUp(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String profilePicUrl) {
        if (InternetUtils.isInternetAvailable((SignUpActivity) signUpView)) {
            signUpInteractor.attemptSignUp(this, username, email, password, profilePicUrl);
        } else {
            signUpView.connectionError();
        }
    }

    @Override
    public void onSignUpSuccess(User user) {
        Prefs.with((SignUpActivity) signUpView).writeBoolean(PreferencesUtils.PREFERENCES_LOGGED_IN, true);
        PreferencesUtils.storeLoggedUser((SignUpActivity) signUpView, user);
        Prefs.with((SignUpActivity) signUpView).writeBoolean(PreferencesUtils.PREFERENCES_NEEDS_EVENTS_SUBSCRIPTION, false);
        signUpView.endLoadingAnimation();
    }

    @Override
    public void onSignUpError(boolean isFailure) {
        if (isFailure) {
            signUpView.connectionError();
        } else {
            signUpView.restorePreAnimationState();
            signUpView.signUpError(((SignUpActivity) signUpView).getString(org.protestr.app.R.string.user_already_exists));
        }
    }

    @Override
    public void onDestroy() {
        signUpView = null;
    }
}
