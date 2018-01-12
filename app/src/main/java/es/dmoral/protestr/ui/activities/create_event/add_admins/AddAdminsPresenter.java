package es.dmoral.protestr.ui.activities.create_event.add_admins;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.User;

/**
 * Created by grender on 11/01/18.
 */

public interface AddAdminsPresenter {
    void filterUsers(String query);
    void onDestroy();
}
