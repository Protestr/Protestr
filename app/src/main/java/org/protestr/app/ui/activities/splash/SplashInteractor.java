package org.protestr.app.ui.activities.splash;

import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 15/02/17.
 */

public interface SplashInteractor {

    interface OnConfirmLoginListener {
        void onLoginConfirmed(User user);

        void onLoginUnconfirmed();
    }

    void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String email, final String password);
}
