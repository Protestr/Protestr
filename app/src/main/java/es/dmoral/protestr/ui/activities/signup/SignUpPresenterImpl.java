package es.dmoral.protestr.ui.activities.signup;

import android.support.annotation.NonNull;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.User;
import es.dmoral.protestr.utils.InternetUtils;
import es.dmoral.protestr.utils.PreferencesUtils;

/**
 * Created by grender on 1/07/17.
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
        signUpView.endLoadingAnimation();
    }

    @Override
    public void onSignUpError(boolean isFailure) {
        if (isFailure) {
            signUpView.connectionError();
        } else {
            signUpView.restorePreAnimationState();
            signUpView.signUpError(((SignUpActivity) signUpView).getString(R.string.email_not_valid_error));
        }
    }

    @Override
    public void onDestroy() {
        signUpView = null;
    }
}
