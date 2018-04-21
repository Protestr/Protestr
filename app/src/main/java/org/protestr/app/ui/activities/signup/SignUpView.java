package org.protestr.app.ui.activities.signup;

/**
 * Created by someone on 1/07/17.
 */

public interface SignUpView {
    void signUpError(final String message);

    void startAnimation();

    void restorePreAnimationState();

    void endLoadingAnimation();

    void connectionError();

    boolean checkParametersErrors();

    void openTermsAndConditions();
}
