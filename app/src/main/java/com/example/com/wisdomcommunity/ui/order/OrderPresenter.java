package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;

import com.example.com.wisdomcommunity.mvp.OrderContract;

/**
 * Created by rhm on 2018/2/26.
 */

public class OrderPresenter extends OrderContract.Presenter {
    public OrderPresenter(Context context, OrderContract.View view) {
        super(context, view);
    }

    @Override
    public void loadOrderRecord() {

    }
}
