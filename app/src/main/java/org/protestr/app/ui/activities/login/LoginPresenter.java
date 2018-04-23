package org.protestr.app.ui.activities.login;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 13/02/17.
 */

public interface LoginPresenter {
    void attemptLogin(@NonNull final String email, @NonNull final String password);

    void resubscribeToEventsIfNeeded(User user);

    void onDestroy();
}
