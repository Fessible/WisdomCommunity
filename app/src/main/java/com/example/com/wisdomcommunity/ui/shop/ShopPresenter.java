package com.example.com.wisdomcommunity.ui.shop;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.support_business.module.ListEntity;
import com.example.com.wisdomcommunity.mvp.ShopContract;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rhm on 2018/2/24.
 */

public class ShopPresenter extends ShopContract.Presenter {

    public ShopPresenter(Context context, ShopContract.View view) {
        super(context, view);
    }

    @Override
    public void loadShopList(boolean refresh) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).shopList(compositeTag, refresh, new RestyServer.SSOCallback<ListEntity<ShopList>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ListEntity<ShopList> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoadShopListSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.onLoadShopListFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoadShopListFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.onLoadShopListFailure(networkError);
                }
            }
        });
    }
}
