package es.dmoral.protestr.ui.fragments.events;

/**
 * Created by grender on 16/02/17.
 */

public interface EventsPresenter {

    void getNewEvents(int offset, int limit, String order);
    void onDestroy();
}
