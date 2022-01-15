package org.protestr.app.ui.fragments.events;

import android.view.Menu;

import org.protestr.app.data.models.dao.Event;

import java.util.ArrayList;

import org.protestr.app.data.models.dao.Event;

/**
 * Created by someone on 16/02/17.
 */

public interface EventsFragmentView {

    void populateNewEventList(ArrayList<Event> newEvents);
    void showError();
    void setFirstMenuItemChecked(Menu menu);
    void loadItems(String order);
}
