package es.dmoral.protestr.ui.activities.create_event.add_admins;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.User;
import es.dmoral.protestr.utils.PreferencesUtils;

/**
 * Created by grender on 11/01/18.
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
        filteredUsers.removeAll(addAdminsView.getAddedAdmins());
        addAdminsView.addFilteredUsers(filteredUsers);
    }
}
