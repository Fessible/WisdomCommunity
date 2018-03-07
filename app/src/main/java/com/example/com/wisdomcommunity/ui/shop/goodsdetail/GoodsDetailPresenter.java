package com.example.com.wisdomcommunity.ui.shop.goodsdetail;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.mvp.GoodsDetailContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/6.
 */

public class GoodsDetailPresenter extends GoodsDetailContract.Presenter {
    public GoodsDetailPresenter(Context context, GoodsDetailContract.View view) {
        super(context, view);
    }

    @Override
    public void loadGoodsDetail(String goodsId) {
        if (destroyFlag.get()) {
            return;
        }
        CommunityServer.with(context).goodsDetail(compositeTag, goodsId, false, new RestyServer.SSOCallback<ResultEntity<GoodsDetail>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<GoodsDetail> entity) {
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.loadGoodsDetailSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.loadGoodsDtailFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.loadGoodsDtailFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.loadGoodsDtailFailure(networkError);
                }
            }
        });
    }
}
