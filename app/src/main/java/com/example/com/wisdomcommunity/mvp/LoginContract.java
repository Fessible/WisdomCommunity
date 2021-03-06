package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/1/16.
 */

public interface LoginContract extends BasicContract {
    public interface Model extends BasicContract.Model {

    }

    public interface View extends BasicContract.View {
        public void onLoginSuccess(CharSequence phone, CharSequence password, String userId);

        public void onLoginFailure(String msg);

    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void login(CharSequence phone, CharSequence password);
    }

}
