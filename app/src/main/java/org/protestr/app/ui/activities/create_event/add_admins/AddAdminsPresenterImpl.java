package org.protestr.app.ui.activities.create_event.add_admins;

import android.support.annotation.NonNull;

import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 11/01/18.
 */

public class AddAdminsPresenterImpl implements AddAdminsPresenter, AddAdminsInteractor.OnUsersFilteredListener {
    private AddAdminsView addAdminsView;
    private AddAdminsInteractor addAdminsInteractor;

    AddAdminsPresenterImpl(@NonNull AddAdminsView addAdminsView) {
        this.addAdminsView = addAdminsView;
        addAdminsInteractor = new AddAdminsInteractorImpl();
    }
    @Override
    public void filterUsers(String query) {
        addAdminsInteractor.filterUsers(this, query);
    }

    @Override
    public void onDestroy() {
        addAdminsView = null;
    }

    @Override
    public void onUsersFiltered(ArrayList<User> filteredUsers) {
        if (addAdminsView != null) {
            if (filteredUsers == null)
                filteredUsers = new ArrayList<>();
            filteredUsers.removeAll(addAdminsView.getAddedAdmins());
            addAdminsView.addFilteredUsers(filteredUsers);
        }
    }
}
