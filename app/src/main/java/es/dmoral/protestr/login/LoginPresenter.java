package es.dmoral.protestr.login;

import android.support.annotation.NonNull;

/**
 * Created by grender on 13/02/17.
 */

public interface LoginPresenter {

    void attemptLogin(@NonNull final String username, @NonNull final String password);
    void attemptSignUp(@NonNull final String username, @NonNull final String password);
    void onDestroy();
}
