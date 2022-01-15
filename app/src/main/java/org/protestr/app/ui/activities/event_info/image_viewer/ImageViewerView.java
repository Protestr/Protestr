package org.protestr.app.ui.activities.event_info.image_viewer;

/**
 * Created by someone on 7/09/17.
 */

public interface ImageViewerView {
    void setImmersiveMode();

    void imageSaved(String filePath);

    void imageError();

    boolean openContextualMenu();
}
