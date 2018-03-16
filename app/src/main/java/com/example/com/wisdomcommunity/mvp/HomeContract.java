package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.home.Banner;
import com.example.com.support_business.domain.home.Home;
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
        void onLoadRecommendSuccess(Home home);

        void onLoadRecommendFailure(String msg);

        void onLoadBannerSuccess(List<Banner> bannerList);

        void onLoadBannerFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadRecomends(boolean refresh);

        public abstract void loadBanners(boolean refresh);
    }

}
