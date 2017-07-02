package es.dmoral.protestr.signup;

import android.support.annotation.NonNull;

import es.dmoral.protestr.R;
import es.dmoral.protestr.login.LoginActivity;
import es.dmoral.protestr.utils.InternetUtils;
import es.dmoral.protestr.utils.UsernameGenerator;

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
    public void attemptSignUp(@NonNull String email, @NonNull String password) {
        if (InternetUtils.isInternetAvailable((LoginActivity) signUpView)) {
            signUpView.showProgress();
            final String username = UsernameGenerator.generateUsername((LoginActivity) signUpView);
            signUpInteractor.attemptSignUp(this, username, email, password);
        } else {
            signUpView.connectionError();
        }
    }

    @Override
    public void onSignUpSuccess(final String email, final String password) {

    }

    @Override
    public void onSignUpError(boolean isFailure) {
        signUpView.hideProgress();
        if (isFailure)
            signUpView.connectionError();
        else
            signUpView.signUpError(((LoginActivity) signUpView).getString(R.string.email_not_valid_error));
    }

    @Override
    public void onDestroy() {

    }
}
