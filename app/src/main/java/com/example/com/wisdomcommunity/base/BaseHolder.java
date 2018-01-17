package com.example.com.wisdomcommunity.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by rhm on 2018/1/17.
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder{
    private Unbinder unbinder;

    public BaseHolder(Context context, ViewGroup parent, @LayoutRes int adapterLayoutResId) {
        super(LayoutInflater.from(context).inflate(adapterLayoutResId, parent, false));
        unbinder = ButterKnife.bind(this, itemView);
    }
}
