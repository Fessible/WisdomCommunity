package com.example.com.wisdomcommunity.ui.category;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.home.Category;
import com.example.com.support_business.module.ListEntity;
import com.example.com.wisdomcommunity.mvp.CategoryContract;

import java.util.Date;

/**
 * Created by rhm on 2018/4/11.
 */

public class CategoryPresenter extends CategoryContract.Presenter {
    public CategoryPresenter(Context context, CategoryContract.View view) {
        super(context, view);
    }

    @Override
    public void onShopList(int type) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).category(compositeTag, false,type, new RestyServer.SSOCallback<ListEntity<Category>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ListEntity<Category> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoadCategorySuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.onLoadCategoryFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoadCategoryFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.onLoadCategoryFailure(networkError);
                }

            }
        });

    }
}
