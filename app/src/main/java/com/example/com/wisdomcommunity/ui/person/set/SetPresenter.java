package com.example.com.wisdomcommunity.ui.person.set;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.domain.personal.Version;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.mvp.SetContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/26.
 */

public class SetPresenter extends SetContract.Presenter {
    public SetPresenter(Context context, SetContract.View view) {
        super(context, view);
    }

    @Override
    public void version(int curVersion) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).version(compositeTag, curVersion, new RestyServer.SSOCallback<ResultEntity<Version>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<Version> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.versionSuccess(entity.result,entity.msg);
                        }
                    } else {
                        if (view != null) {
                            view.versionFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.versionFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.versionFailure(networkError);
                }
            }
        });
    }
}
