package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/3.
 */

public interface OrderDetailContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void onLoadDetailSuccess(OrderDetail orderDetail);

        void onLoadDetailFailure(String msg);
    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadDetail(String orderId);
    }

}
