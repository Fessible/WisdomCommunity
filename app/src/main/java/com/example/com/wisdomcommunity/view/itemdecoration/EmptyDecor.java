package com.example.com.wisdomcommunity.view.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class EmptyDecor implements FlexibleDecor {

    @Override
    public void decorRect(Rect outRect, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation) {

    }

    @Override
    public void decorDraw(Canvas canvas, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation) {

    }

    @Override
    public void decorDrawOver(Canvas canvas, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation) {

    }
}
