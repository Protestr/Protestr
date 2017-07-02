package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

import es.dmoral.protestr.api.WebService;
import es.dmoral.protestr.models.models.ResponseStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 13/02/17.
 */

class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String email, @NonNull final String password) {
        WebService.getInstance().getApiInterface().attemptLogin(email, password).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200 && response.body().getStatus() == ResponseStatus.STATUS_OK)
                    onAttemptLoginListener.onLoginSuccess(email, password);
                else
                    onAttemptLoginListener.onLoginError(false);
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onAttemptLoginListener.onLoginError(true);
            }
        });
    }
}
