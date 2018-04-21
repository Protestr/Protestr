package org.protestr.app.ui.fragments.events;

import org.protestr.app.data.models.dao.User;

/**
 * Created by someone on 16/02/17.
 */

public interface EventsPresenter {

    void getNewEvents(int offset, int limit, String order);

    void onDestroy();
}
