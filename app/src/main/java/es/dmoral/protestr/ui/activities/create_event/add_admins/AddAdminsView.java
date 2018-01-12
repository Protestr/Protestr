package es.dmoral.protestr.ui.activities.create_event.add_admins;

import android.os.Bundle;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.User;

/**
 * Created by grender on 11/01/18.
 */

public interface AddAdminsView {
    void restoreStates(Bundle savedInstanceState);
    void addFilteredUsers(ArrayList<User> filteredUsers);
    ArrayList<User> getAddedAdmins();
    void backToEventCreation();
}
