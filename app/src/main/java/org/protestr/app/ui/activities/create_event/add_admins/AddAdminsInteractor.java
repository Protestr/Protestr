package org.protestr.app.ui.activities.create_event.add_admins;

import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 11/01/18.
 */

public interface AddAdminsInteractor {
    interface OnUsersFilteredListener {
        void onUsersFiltered(ArrayList<User> filteredUsers);
    }

    void filterUsers(OnUsersFilteredListener onUsersFilteredListener, String query);
}
