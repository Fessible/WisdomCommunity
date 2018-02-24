package com.example.com.wisdomcommunity.ui.home;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.support_business.module.ListEntity;
import com.example.com.wisdomcommunity.mvp.HomeContract;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rhm on 2018/1/17.
 */

public class HomePresenter extends HomeContract.Presenter {
    private final String compositeTag = UUID.randomUUID().toString();
    private final AtomicBoolean destroyFlag = new AtomicBoolean(false);

    public HomePresenter(Context context, HomeContract.View view) {
        super(context, view);
    }

    @Override
    public void loadRecomends(boolean refresh) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).recommend(compositeTag, refresh, new RestyServer.SSOCallback<ListEntity<Recommend>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ListEntity<Recommend> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoadRecommendSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.onLoadRecommendFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoadRecommendFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.onLoadRecommendFailure(networkError);
                }

            }
        });

    }
}
