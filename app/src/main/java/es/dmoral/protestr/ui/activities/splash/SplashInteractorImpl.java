package es.dmoral.protestr.ui.activities.splash;

import es.dmoral.protestr.data.api.WebService;
import es.dmoral.protestr.data.models.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 15/02/17.
 */

class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String username, final String password) {
        WebService.getInstance().getApiInterface().attemptLogin(username, password).enqueue(new Callback<User>() {
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
