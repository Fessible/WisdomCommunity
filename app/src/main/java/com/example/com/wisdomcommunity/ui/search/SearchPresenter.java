package com.example.com.wisdomcommunity.ui.search;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.search.Search;
import com.example.com.support_business.module.ListEntity;
import com.example.com.wisdomcommunity.mvp.SearchContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/8.
 */

public class SearchPresenter extends SearchContract.Presenter {
    public SearchPresenter(Context context, SearchContract.View view) {
        super(context, view);
    }

    @Override
    public void loadSearch(final String search) {
        if (destroyFlag.get()) {
            return;
        }

        CommunityServer.with(context).search(compositeTag, search, new RestyServer.SSOCallback<ListEntity<Search>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ListEntity<Search> entity) {
                if (entity != null) {
                    if (entity != null) {
                        if (entity.isOk()) {
                            if (view != null) {
                                view.loadSearchSuccess(entity.result);
                            }
                        } else {
                            if (view != null) {
                                view.loadSearchFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                            }
                        }
                    } else {
                        if (view != null) {
                            view.loadSearchFailure(networkError);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.loadSearchFailure(networkError);
                }
            }
        });
    }
}
