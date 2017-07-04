package es.dmoral.protestr.signup;

import android.support.annotation.NonNull;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.R;
import es.dmoral.protestr.login.LoginActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.InternetUtils;

/**
 * Created by grender on 1/07/17.
 */

public class SignUpPresenterImpl implements SignUpPresenter, SignUpInteractor.OnAttemptSignUpListener {

    private SignUpView signUpView;
    private SignUpInteractor signUpInteractor;

    public SignUpPresenterImpl (@NonNull SignUpView signUpView) {
        this.signUpView = signUpView;
        signUpInteractor = new SignUpInteractorImpl();
    }

    @Override
    public void attemptSignUp(@NonNull String username, @NonNull String email, @NonNull String password) {
        if (InternetUtils.isInternetAvailable((SignUpActivity) signUpView)) {
            signUpInteractor.attemptSignUp(this, username, email, password);
        } else {
            signUpView.connectionError();
        }
    }

    @Override
    public void onSignUpSuccess(final String email, final String password) {
        Prefs.with((SignUpActivity) signUpView).writeBoolean(Constants.PREFERENCES_LOGGED_IN, true);
        Prefs.with((SignUpActivity) signUpView).write(Constants.PREFERENCES_EMAIL, email);
        Prefs.with((SignUpActivity) signUpView).write(Constants.PREFERENCES_PASSWORD, password);
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
