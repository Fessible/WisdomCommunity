package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;

import com.example.com.wisdomcommunity.mvp.OrderDetailContract;

/**
 * Created by rhm on 2018/3/3.
 */

public class OrderDetailPresenter extends OrderDetailContract.Presenter {
    public OrderDetailPresenter(Context context, OrderDetailContract.View view) {
        super(context, view);
    }

    @Override
    public void loadDetail(String orderId) {

    }
}
