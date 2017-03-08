package es.dmoral.protestr.splash;

/**
 * Created by grender on 15/02/17.
 */

public interface SplashInteractor {
    interface OnConfirmLoginListener {
        void onLoginConfirmed();
        void onLoginUnconfirmed();
    }

    void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String username, final String password);
}
