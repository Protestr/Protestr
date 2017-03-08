package es.dmoral.protestr.splash;

import es.dmoral.protestr.api.WebService;
import es.dmoral.protestr.api.models.ResponseStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 15/02/17.
 */

class SplashInteractorImpl implements SplashInteractor {
    @Override
    public void confirmLogin(final OnConfirmLoginListener onConfirmLoginListener, final String username, final String password) {
        new WebService().getApiInterface().attemptLogin(username, password).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200 && response.body().getStatus() == ResponseStatus.STATUS_OK)
                    onConfirmLoginListener.onLoginConfirmed();
                else
                    onConfirmLoginListener.onLoginUnconfirmed();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onConfirmLoginListener.onLoginUnconfirmed();
            }
        });
    }
}
