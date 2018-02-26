package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.support_business.module.ListEntity;
import com.example.com.wisdomcommunity.mvp.OrderContract;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rhm on 2018/2/26.
 */

public class OrderPresenter extends OrderContract.Presenter {
    private String compositeTag = UUID.randomUUID().toString();
    private AtomicBoolean destroyFlag = new AtomicBoolean(false);

    public OrderPresenter(Context context, OrderContract.View view) {
        super(context, view);
    }

    @Override
    public void loadOrderRecord() {
        if (destroyFlag.get()) {
            return;
        }

        showProgress();
        CommunityServer.with(context).record(compositeTag, false, new RestyServer.SSOCallback<ListEntity<OrderRecord>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ListEntity<OrderRecord> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.loadOrderRecordSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.loadorderRecordFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.loadorderRecordFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.loadorderRecordFailure(networkError);
                }
            }
        });

    }
}
