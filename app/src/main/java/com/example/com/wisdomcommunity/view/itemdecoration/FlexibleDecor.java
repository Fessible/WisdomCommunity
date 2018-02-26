package com.example.com.wisdomcommunity.view.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface FlexibleDecor {

    @IntDef({
            LinearLayoutManager.HORIZONTAL,
            LinearLayoutManager.VERTICAL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {}

    public void decorRect(Rect outRect, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation);
    public void decorDraw(Canvas canvas, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation);
    public void decorDrawOver(Canvas canvas, RecyclerView parent, View child, FlexibleEdge flexibleEdge, @Orientation int orientation);
}
