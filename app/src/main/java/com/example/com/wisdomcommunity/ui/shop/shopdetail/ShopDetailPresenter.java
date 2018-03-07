package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.mvp.ShopDetailContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/6.
 */

public class ShopDetailPresenter extends ShopDetailContract.Presenter {
    public ShopDetailPresenter(Context context, ShopDetailContract.View view) {
        super(context, view);
    }

    @Override
    public void loadShopDetail(String shopId) {
        if (destroyFlag.get()) {
            return;
        }
        CommunityServer.with(context).shopDetail(compositeTag, false, shopId, new RestyServer.SSOCallback<ResultEntity<ShopDetail>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<ShopDetail> entity) {
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoadShopDetailSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.onLoadShopDetailFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoadShopDetailFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.onLoadShopDetailFailure(networkError);
                }
            }
        });
    }
}
