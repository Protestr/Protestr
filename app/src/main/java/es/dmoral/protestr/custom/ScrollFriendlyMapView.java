package es.dmoral.protestr.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/**
 * Created by grender on 17/06/17.
 */

public class ScrollFriendlyMapView extends MapView {
    public ScrollFriendlyMapView(Context context) {
        super(context);
    }

    public ScrollFriendlyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ScrollFriendlyMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ScrollFriendlyMapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Disallow ScrollView to intercept touch events.
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        // Handle MapView's touch events.
        super.dispatchTouchEvent(ev);
        return true;
    }
}
