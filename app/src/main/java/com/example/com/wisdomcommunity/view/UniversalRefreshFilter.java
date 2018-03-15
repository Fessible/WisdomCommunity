package com.example.com.wisdomcommunity.view;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class UniversalRefreshFilter implements SwipeRefreshLayout.RefreshFilter {

    @Override
    public boolean captureChild(View child) {
        return child != null;
    }

    @Override
    public boolean canChildScrollUp(ViewGroup parent, View child) {
        if (child != null) {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (child instanceof AbsListView) {
                    final AbsListView absListView = (AbsListView) child;
                    return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
                } else {
                    return ViewCompat.canScrollVertically(child, -1) || child.getScrollY() > 0;
                }
            } else {
                return ViewCompat.canScrollVertically(child, -1);
            }
        }
        return true;
    }
}
