package es.dmoral.protestr.ui.activities.login;

import android.support.annotation.NonNull;

import es.dmoral.protestr.data.api.WebService;
import es.dmoral.protestr.data.models.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 13/02/17.
 */

class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String email, @NonNull final String password) {
        WebService.getInstance().getApiInterface().attemptLogin(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200)
                    onAttemptLoginListener.onLoginSuccess(response.body());
                else
                    onAttemptLoginListener.onLoginError(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onAttemptLoginListener.onLoginError(true);
            }
        });
    }
}
