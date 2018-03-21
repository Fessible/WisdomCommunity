package com.example.com.wisdomcommunity.ui.login;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.login.User;
import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.support_business.module.ListEntity;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.mvp.LoginContract;
import com.example.com.wisdomcommunity.util.VerifyUtils;

import java.util.Date;

/**
 * Created by rhm on 2018/3/21.
 */

public class LoginPresenter extends LoginContract.Presenter {
    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
    }

    @Override
    public void login(final CharSequence phone, final CharSequence password) {
        if (destroyFlag.get()) {
            return;
        }

        showProgress();
        CommunityServer.with(context).login(compositeTag, phone.toString(), password.toString(), new RestyServer.SSOCallback<ResultEntity<User>>() {
            @Override
            public void onUnauthorized() {
            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ResultEntity<User> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoginSuccess(phone, password, entity.result.userId);
                        }
                    } else {
                        if (view != null) {
                            view.onLoginFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoginFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.onLoginFailure(networkError);
                }
            }
        });
    }

}
