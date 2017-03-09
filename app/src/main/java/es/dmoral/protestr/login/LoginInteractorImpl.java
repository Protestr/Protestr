package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

import es.dmoral.protestr.api.WebService;
import es.dmoral.protestr.api.models.ResponseStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 13/02/17.
 */

class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String username, @NonNull final String password) {
        new WebService().getApiInterface().attemptLogin(username, password).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200 && response.body().getStatus() == ResponseStatus.STATUS_OK)
                    onAttemptLoginListener.onLoginSuccess(username, password);
                else
                    onAttemptLoginListener.onLoginError(false);
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onAttemptLoginListener.onLoginError(true);
            }
        });
    }

    @Override
    public void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username, @NonNull final String password) {
        new WebService().getApiInterface().attemptSignup(username, password).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200 && response.body().getStatus() == ResponseStatus.STATUS_OK)
                    onAttemptSignUpListener.onSignUpSuccess(username, password);
                else
                    onAttemptSignUpListener.onSignUpError(false);
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                onAttemptSignUpListener.onSignUpError(true);
            }
        });
    }
}
