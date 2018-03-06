package com.example.com.wisdomcommunity.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by rhm on 2018/2/26.
 */

public abstract class BaseAdapter<E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<E> {
    protected abstract void destroy();
}
