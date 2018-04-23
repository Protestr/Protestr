package org.protestr.app.ui.activities.login;

import android.support.annotation.NonNull;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by someone on 13/02/17.
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

    @Override
    public void getSubscribedEvents(final OnSubscribedEventsReceivedListener onSubscribedEventsReceivedListener, String userEmail, String userPassword) {
        WebService.getInstance().getApiInterface().getSubscribedEvents(userEmail, userPassword).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                onSubscribedEventsReceivedListener.onSubscribedEventsReceived(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                onSubscribedEventsReceivedListener.onSubscribedEventsReceivedError();
            }
        });
    }
}
