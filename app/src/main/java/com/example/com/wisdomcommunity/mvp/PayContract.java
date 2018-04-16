package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/4/15.
 */
public interface PayContract {
    public interface Model extends BasicContract.Model {
    }

    interface View extends BasicContract.View {
        void onSubmitOrderSuccess(String msg,OrderDetail orderDetail);

        void onSubmitOrderFailure(String msg);
    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void submitOrder(OrderDetail orderDetail);
    }


}
