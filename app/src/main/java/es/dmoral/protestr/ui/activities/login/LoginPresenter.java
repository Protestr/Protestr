package es.dmoral.protestr.ui.activities.login;

import android.support.annotation.NonNull;

/**
 * Created by grender on 13/02/17.
 */

public interface LoginPresenter {
    void attemptLogin(@NonNull final String email, @NonNull final String password);

    void onDestroy();
}
