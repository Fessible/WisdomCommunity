package com.example.com.wisdomcommunity.ui.register;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.module.Entity;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.mvp.RegisterContract;
import com.example.com.wisdomcommunity.util.VerifyUtils;

import java.util.Date;

/**
 * Created by rhm on 2018/3/21.
 */

public class RegisterPresenter extends RegisterContract.Presenter {
    public RegisterPresenter(Context context, RegisterContract.View view) {
        super(context, view);
    }

    @Override
    public void register(String userName, String sms, final String phone, final String password) {
        if (checkPass(password)) {
            if (destroyFlag.get()) {
                return;
            }

            showProgress();
            CommunityServer.with(context).register(compositeTag,userName, phone, password,sms, new RestyServer.SSOCallback<Entity>() {
                @Override
                public void onUnauthorized() {

                }

                @Override
                public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                    hideProgress();
                    if (entity != null) {
                        if (entity.isOk()) {
                            if (view != null) {
                                view.registerSuccess(phone, password, entity.msg);
                            }
                        } else {
                            if (view != null) {
                                view.regitsterFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                            }
                        }
                    } else {
                        if (view != null) {
                            view.regitsterFailure(networkError);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    hideProgress();
                    if (view != null) {
                        view.regitsterFailure(networkError);
                    }
                }
            });
        }

    }

    private boolean checkPass(final CharSequence password) {

        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 12) {
            // 密码长度小于6或大于12
            Toast.makeText(context, R.string.password_length_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!VerifyUtils.matcherPassword(String.valueOf(password), 6, 12, true)) {
            // 密码格式有误
            Toast.makeText(context, R.string.password_error_format, Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }
}
