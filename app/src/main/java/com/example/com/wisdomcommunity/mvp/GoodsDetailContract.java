package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/2/24.
 */

public interface GoodsDetailContract extends BasicContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void loadGoodsDetailSuccess(GoodsDetail goodsDetail);

        void loadGoodsDtailFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadGoodsDetail(String goodsId);
    }

}
