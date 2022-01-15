package org.protestr.app.ui.activities.create_event.add_admins;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

import org.protestr.app.data.api.WebService;
import org.protestr.app.data.models.dao.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by someone on 11/01/18.
 */

public class AddAdminsInteractorImpl implements AddAdminsInteractor {
    @Override
    public void filterUsers(final OnUsersFilteredListener onUsersFilteredListener, String query) {
        WebService.getInstance().getApiInterface().filterUsers(query).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                onUsersFilteredListener.onUsersFiltered(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                // ignored
            }
        });
    }
}
