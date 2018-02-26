package com.example.com.wisdomcommunity.view.itemdecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class DividerDecor implements FlexibleDecor {

    private static final int[] ATTRS = {
            //
            android.R.attr.divider
    };

    static {
        Arrays.sort(ATTRS);
    }

    private Context context;
    private int startBorder;
    private int endBorder;

    private boolean footerEnable;

    private Drawable divider;

    private DividerDecor(Context context, int startBorder, int endBorder, boolean footerEnable) {
        super();

        this.context = context;

        this.startBorder = startBorder;
        this.endBorder = endBorder;

        this.footerEnable = footerEnable;

        TypedArray array = context.obtainStyledAttributes(null, ATTRS, android.R.attr.listViewStyle, 0);
        divider = array.getDrawable(Arrays.binarySearch(ATTRS, android.R.attr.divider));
        array.recycle();
    }

    public void setDivider(Drawable divider) {
        this.divider = divider;
    }

    @Override
    public void decorRect(Rect outRect, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation) {
        switch (orientation) {
            case LinearLayoutManager.HORIZONTAL: {
                final int left = 0;
                final int top = 0;
                final int right = flexibleEdge.isFooterRow && !footerEnable ? 0 : divider.getIntrinsicWidth();
                final int bottom = 0;
                outRect.set(left, top, right, bottom);
                break;
            }
            case LinearLayoutManager.VERTICAL: {
                final int left = 0;
                final int top = 0;
                final int right = 0;
                final int bottom = flexibleEdge.isFooterRank && !footerEnable ? 0 : divider.getIntrinsicHeight();
                outRect.set(left, top, right, bottom);
                break;
            }
        }
    }

    @Override
    public void decorDraw(Canvas canvas, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation) {
        canvas.save();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        Rect outRect = new Rect();
        decorRect(outRect, parent, child, flexibleEdge, orientation);

        switch (orientation) {
            case LinearLayoutManager.HORIZONTAL: {
                final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                final int top = child.getTop();
                final int right = left + outRect.right;
                final int bottom = child.getBottom();
                divider.setBounds(left, top + startBorder, right, bottom - endBorder);
                divider.draw(canvas);
                break;
            }
            case LinearLayoutManager.VERTICAL: {
                final int left = child.getLeft();
                final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                final int right = child.getRight();
                final int bottom = top + outRect.bottom;
                divider.setBounds(left + startBorder, top, right - endBorder, bottom);
                divider.draw(canvas);
                break;
            }
        }

        canvas.restore();
    }

    @Override
    public void decorDrawOver(Canvas canvas, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation) {

    }

    public static class Builder {

        private Context context;

        private int startBorder;
        private int endBorder;
        private boolean footerEnable = false;

        private Drawable divider;

        public Builder(Context context) {
            super();
            this.context = context;
        }

        public Builder startBorder(int border) {
            this.startBorder = border;
            return this;
        }

        public Builder endBorder(int border) {
            this.endBorder = border;
            return this;
        }

        public Builder startBorderResId(@DimenRes int borderResId) {
            this.startBorder = context.getResources().getDimensionPixelOffset(borderResId);
            return this;
        }

        public Builder endBorderResId(@DimenRes int borderResId) {
            this.endBorder = context.getResources().getDimensionPixelOffset(borderResId);
            return this;
        }

        public Builder footerEnable(boolean footerEnable) {
            this.footerEnable = footerEnable;
            return this;
        }

        public Builder divider(Drawable divider) {
            this.divider = divider;
            return this;
        }

        public Builder dividerResId(@DrawableRes int resId) {
            this.divider = context.getResources().getDrawable(resId);
            return this;
        }

        public DividerDecor build() {
            DividerDecor dividerDecor = new DividerDecor(context, startBorder, endBorder, footerEnable);
            if (divider != null) {
                dividerDecor.setDivider(divider);
            }
            return dividerDecor;
        }
    }
}
