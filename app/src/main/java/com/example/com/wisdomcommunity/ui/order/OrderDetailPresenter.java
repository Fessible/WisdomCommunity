package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.mvp.OrderDetailContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/3.
 */

public class OrderDetailPresenter extends OrderDetailContract.Presenter {
    public OrderDetailPresenter(Context context, OrderDetailContract.View view) {
        super(context, view);
    }

    @Override
    public void loadDetail(String orderId) {
        CommunityServer.with(context).orderDetail(compositeTag, false, orderId, new RestyServer.SSOCallback<ResultEntity<OrderDetail>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<OrderDetail> entity) {
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoadDetailSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.onLoadDetailFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoadDetailFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.onLoadDetailFailure(networkError);
                }
            }
        });

    }
}
