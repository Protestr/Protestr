package es.dmoral.protestr.ui.activities.event_info.image_viewer;

/**
 * Created by grender on 7/09/17.
 */

public interface ImageViewerView {
    void setImmersiveMode();
    void imageSaved(String filePath);
    void imageError();
    boolean openContextualMenu();
}
