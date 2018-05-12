package org.protestr.app.ui.activities.live_feed;

public interface LiveFeedView {
    void sendEventUpdate();

    void setTextBarVisibility(int position);

    void animateTextBar(boolean isHiding, int position);
}
