package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.personal.Version;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/26.
 */

public interface SetContract {
    public interface Model extends BasicContract.Model {

    }

    public interface View extends BasicContract.View {
        void versionSuccess(Version version, String msg);

        void versionFailure(String msg);
    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void version(int curVersion);
    }

}
