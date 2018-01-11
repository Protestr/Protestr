package es.dmoral.protestr.ui.activities.create_event.add_admins;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.User;

/**
 * Created by grender on 11/01/18.
 */

public interface AddAdminsInteractor {
    interface OnUsersFilteredListener {
        void onUsersFiltered(ArrayList<User> filteredUsers);
    }

    void filterUsers(OnUsersFilteredListener onUsersFilteredListener, String query);
}
