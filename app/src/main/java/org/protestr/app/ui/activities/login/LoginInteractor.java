package org.protestr.app.ui.activities.login;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.Event;
import org.protestr.app.data.models.dao.User;

import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

/**
 * Created by someone on 13/02/17.
 */

interface LoginInteractor {

    interface OnAttemptLoginListener {
        void onLoginSuccess(User user);

        void onLoginError(boolean isFailure);
    }

    interface OnSubscribedEventsReceivedListener {
        void onSubscribedEventsReceived(ArrayList<Event> events);

        void onSubscribedEventsReceivedError();
    }

    void attemptLogin(final OnAttemptLoginListener onAttemptLoginListener, @NonNull final String email,
                      @NonNull final String password);

    void getSubscribedEvents(OnSubscribedEventsReceivedListener onSubscribedEventsReceivedListener,
                             String userEmail, String userPassword);
}
