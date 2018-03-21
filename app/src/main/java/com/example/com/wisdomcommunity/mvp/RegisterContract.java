package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/21.
 */

public interface RegisterContract {
    public interface Model extends BasicContract.Model {

    }

    public interface View extends BasicContract.View {
    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }
    }

}
