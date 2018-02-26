package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

import java.util.List;

/**
 * Created by rhm on 2018/2/24.
 */

public interface ShopContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void onLoadShopListSuccess(List<ShopList> shopList);

        void onLoadShopListFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadShopList(boolean refresh);
    }

}
