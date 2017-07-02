package es.dmoral.protestr.signup;

import android.support.annotation.NonNull;

import es.dmoral.protestr.api.WebService;
import es.dmoral.protestr.models.models.ResponseStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by grender on 1/07/17.
 */

public class SignUpInteractorImpl implements SignUpInteractor {

    @Override
    public void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username,
                              @NonNull final String email, @NonNull final String password) {
        WebService.getInstance().getApiInterface().attemptSignup(email, username, password).enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200 && response.body().getStatus() == ResponseStatus.STATUS_OK)
                    onAttemptSignUpListener.onSignUpSuccess(email, password);
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
