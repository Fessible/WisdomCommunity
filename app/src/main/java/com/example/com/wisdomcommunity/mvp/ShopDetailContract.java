package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/2/24.
 */

public interface ShopDetailContract extends BasicContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void onLoadShopDetailSuccess(ShopDetail shopDetail);

        void onLoadShopDetailFailure(String msg);


    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadShopDetail(String shopId);


    }

}
