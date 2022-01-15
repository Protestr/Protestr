package org.protestr.app.ui.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by someone on 19/06/17.
 */

public class ScrollFriendlyScrollView extends ScrollView {
    public ScrollFriendlyScrollView(Context context) {
        super(context);
    }

    public ScrollFriendlyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollFriendlyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollFriendlyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

        // Handle this ScrollView touch events.
        super.dispatchTouchEvent(ev);
        return true;
    }
}
