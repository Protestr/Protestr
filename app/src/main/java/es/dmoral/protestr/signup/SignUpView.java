package es.dmoral.protestr.signup;

/**
 * Created by grender on 1/07/17.
 */

public interface SignUpView {
    void showProgress();
    void hideProgress();
    void signUpError(final String message);
    void connectionError();
}
