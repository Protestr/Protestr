package org.protestr.app.ui.custom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by someone on 12/01/18.
 */

public class MarginItemDecorator extends RecyclerView.ItemDecoration {

    private int leftMargin;
    private int rightMargin;

    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };

    private Drawable divider;

    private final Rect mBounds = new Rect();

    public MarginItemDecorator(Context context, int leftMargin, int rightMargin) {
        this.leftMargin = dpToPx(context, leftMargin);
        this.rightMargin = dpToPx(context, rightMargin);
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    public MarginItemDecorator(Context context, int leftMargin) {
        this(context, leftMargin, 0);
    }

    public void setDrawable(@NonNull Drawable drawable) {
        divider = drawable;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || divider == null) {
            return;
        }

        canvas.save();
        final int left;
        final int right;

        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - divider.getIntrinsicHeight();
            divider.setBounds(left + leftMargin, top, right - rightMargin, bottom);
            divider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (divider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        outRect.set(0, 0, 0, divider.getIntrinsicHeight());
    }

    private int dpToPx(Context context, int dp) {
        final Resources resources = context.getResources();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics()));
    }
}
