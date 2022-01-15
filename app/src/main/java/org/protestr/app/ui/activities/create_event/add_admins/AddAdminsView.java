package org.protestr.app.ui.activities.create_event.add_admins;

import android.os.Bundle;

import org.protestr.app.data.models.dao.User;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 11/01/18.
 */

public interface AddAdminsView {
    void restoreStates(Bundle savedInstanceState);
    void addFilteredUsers(ArrayList<User> filteredUsers);
    ArrayList<User> getAddedAdmins();
    void backToEventCreation();
}
