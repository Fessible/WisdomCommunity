package com.example.com.wisdomcommunity.ui.shop.pay;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.module.Entity;
import com.example.com.wisdomcommunity.mvp.PayContract;

import java.util.Date;

/**
 * Created by rhm on 2018/4/15.
 */
public class PayPresenter extends PayContract.Presenter {
    public PayPresenter(Context context, PayContract.View view) {
        super(context, view);
    }

    @Override
    public void submitOrder(final OrderDetail orderDetail) {
        if (destroyFlag.get()) {
            return;
        }
        CommunityServer.with(context).submitOrder(compositeTag, orderDetail, new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onSubmitOrderSuccess(entity.msg,orderDetail);
                        }
                    } else {
                        if (view != null) {
                            view.onSubmitOrderFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onSubmitOrderFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.onSubmitOrderFailure(networkError);
                }
            }
        });
    }
}
