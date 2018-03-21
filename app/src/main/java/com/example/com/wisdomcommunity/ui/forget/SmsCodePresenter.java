package com.example.com.wisdomcommunity.ui.forget;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.module.Entity;
import com.example.com.wisdomcommunity.mvp.SmsCodeContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/21.
 */

public class SmsCodePresenter extends SmsCodeContract.Presenter {
    public SmsCodePresenter(Context context, SmsCodeContract.View view) {
        super(context, view);
    }

    @Override
    public void smsCode(String phone) {
        if (destroyFlag.get()) {
            return;
        }

        showProgress();
        CommunityServer.with(context).sms(compositeTag, phone, new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.sendSMSSuccess(entity.msg);
                        }
                    } else {
                        if (view != null) {
                            view.sendSMSFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.sendSMSFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.sendSMSFailure(networkError);
                }
            }
        });
    }
}
