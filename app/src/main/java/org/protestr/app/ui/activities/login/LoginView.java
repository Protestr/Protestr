package org.protestr.app.ui.activities.login;

/**
 * Created by someone on 13/02/17.
 */

public interface LoginView {
    void showProgress();

    void hideProgress();

    void loginError(final String message);

    void loginSuccess();

    void connectionError();

    void loginAction();

    void signUpAction();
}
