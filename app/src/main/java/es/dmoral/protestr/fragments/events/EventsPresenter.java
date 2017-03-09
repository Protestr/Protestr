package es.dmoral.protestr.fragments.events;

/**
 * Created by grender on 16/02/17.
 */

public interface EventsPresenter {

    void getNewEvents(int offset, int limit);
    void onDestroy();
}
