package es.dmoral.protestr.ui.activities.splash;

import es.dmoral.protestr.data.models.User;

/**
 * Created by grender on 15/02/17.
 */

public interface SplashInteractor {

    interface OnConfirmLoginListener {
        void onLoginConfirmed(User user);

        void onLoginUnconfirmed();
    }

    void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String email, final String password);
}
