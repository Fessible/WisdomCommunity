package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.home.Recommend;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

import java.util.List;

/**
 * Created by rhm on 2018/2/22.
 */

public interface HomeContract {

    interface Model extends BasicContract.Model {
    }

    interface View extends BasicContract.View {
        void onLoadRecommendSuccess(List<Recommend> recommendList);

        void onLoadRecommendFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadRecomends(boolean refresh);
    }

}
