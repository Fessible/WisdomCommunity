package com.example.com.wisdomcommunity.ui.person.feedback;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.module.Entity;
import com.example.com.wisdomcommunity.mvp.FeedbackContract;

import java.util.Date;

/**
 * Created by rhm on 2018/3/2.
 */

public class FeedBbackPresenter extends FeedbackContract.Presenter {
    public FeedBbackPresenter(Context context, FeedbackContract.View view) {
        super(context, view);
    }

    @Override
    public void submitSuggestion(String suggestion) {
        if (destroyFlag.get()) {
            return;
        }
        CommunityServer.with(context).suggestion(compositeTag, suggestion, new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.submitSuccess(entity.msg);
                        }
                    } else {
                        if (view != null) {
                            view.submitFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.submitFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.submitFailure(networkError);
                }
            }
        });
    }
}
