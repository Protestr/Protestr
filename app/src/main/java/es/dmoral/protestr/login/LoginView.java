package es.dmoral.protestr.login;

/**
 * Created by grender on 13/02/17.
 */

public interface LoginView {

    void showProgress();
    void hideProgress();
    void loginError(final String message);
    void loginSuccess();
    void signUpError(final String message);
    void showSignUpConfirmation(final String username, final String password);
    void connectionError();
}
