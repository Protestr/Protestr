package es.dmoral.protestr.ui.fragments.events;

import es.dmoral.protestr.data.models.dao.User;

/**
 * Created by grender on 16/02/17.
 */

public interface EventsPresenter {

    void getNewEvents(int offset, int limit, String order);

    void onDestroy();
}
