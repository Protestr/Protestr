package org.protestr.app.ui.activities.signup;

import android.support.annotation.NonNull;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by someone on 1/07/17.
 */

public class SignUpInteractorImpl implements SignUpInteractor {

    @Override
    public void attemptSignUp(final OnAttemptSignUpListener onAttemptSignUpListener, @NonNull final String username,
                              @NonNull final String email, @NonNull final String password, @NonNull final String profilePicUrl) {
        WebService.getInstance().getApiInterface().attemptSignup(email, username, password, profilePicUrl).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200)
                    onAttemptSignUpListener.onSignUpSuccess(response.body());
                else
                    onAttemptSignUpListener.onSignUpError(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onAttemptSignUpListener.onSignUpError(true);
            }
        });
    }
}
