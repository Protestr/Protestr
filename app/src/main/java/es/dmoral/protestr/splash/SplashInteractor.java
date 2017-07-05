package es.dmoral.protestr.splash;

import es.dmoral.protestr.models.models.User;

/**
 * Created by grender on 15/02/17.
 */

public interface SplashInteractor {

    interface OnConfirmLoginListener {
        void onLoginConfirmed(User user);
        void onLoginUnconfirmed();
    }

    void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String username, final String password);
}
