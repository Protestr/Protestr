package org.protestr.app.ui.activities.splash;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by someone on 15/02/17.
 */

class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String email, final String password) {
        WebService.getInstance().getApiInterface().attemptLogin(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200)
                    onConfirmLoginListener.onLoginConfirmed(response.body());
                else
                    onConfirmLoginListener.onLoginUnconfirmed();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onConfirmLoginListener.onLoginUnconfirmed();
            }
        });
    }
}
