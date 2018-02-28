package com.example.com.wisdomcommunity.ui.person.info;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.mvp.EditInfoContract;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rhm on 2018/2/28.
 */

public class EditInfoPresenter extends EditInfoContract.Presenter {
    private String compositeTag = UUID.randomUUID().toString();
    private AtomicBoolean destroyFlag = new AtomicBoolean(false);

    public EditInfoPresenter(Context context, EditInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void loadInfo(String userId) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).info(compositeTag, false, userId, new RestyServer.SSOCallback<ResultEntity<Info>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<Info> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.loadInfoSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.loadInfoFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.loadInfoFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.loadInfoFailure(networkError);
                }
            }
        });
    }
}
