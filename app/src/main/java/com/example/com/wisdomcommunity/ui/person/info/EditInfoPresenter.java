package com.example.com.wisdomcommunity.ui.person.info;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.personal.HeadImage;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.module.Entity;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.support_business.params.PersonParams;
import com.example.com.wisdomcommunity.mvp.EditInfoContract;

import java.io.File;
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
    public void editHeadImage(File headImage) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).editHeadImage(compositeTag, headImage, new RestyServer.SSOCallback<ResultEntity<HeadImage>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<HeadImage> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.editHeadImageSuccess(entity.result, entity.msg);
                        }
                    } else {
                        if (view != null) {
                            view.editHeadImageFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.editHeadImageFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.editHeadImageFailure(networkError);
                }
            }
        });
    }

    @Override
    public void editInfo(PersonParams info) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).editInfo(compositeTag, info, new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.editInfoSuccess(entity.msg);
                        }
                    } else {
                        if (view != null) {
                            view.editInfoFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.editInfoFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.editInfoFailure(networkError);
                }
            }
        });
    }
}
