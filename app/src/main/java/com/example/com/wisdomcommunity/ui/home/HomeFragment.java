package com.example.com.wisdomcommunity.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.HomeContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 主页
 * Created by rhm on 2018/1/16.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.banner)
    BGABanner banner;

    private HomePresenter presenter;

    //test
    private List<String> imgUrlList = new ArrayList<>();
    private List<String> imgTipList = new ArrayList<>();
    private List<Recommend> recommendList = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        presenter = new HomePresenter(getContext(), HomeFragment.this);
        presenter.loadRecomends(false);
//        initData();
        adapter = new HomeAdapter(getContext());
//        adapter.setData(recommendList);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        banner.setData(imgUrlList, imgTipList);
        banner.setAdapter(bannerAdapter);

        //点击事件
        banner.setDelegate(delegate);
    }

    private void initData() {
        imgTipList = Arrays.asList("", "", "");
        imgUrlList = Arrays.asList("http://ww1.sinaimg.cn/large/83029c1egy1fni7wwcrrdj20go0ciq4x.jpg", "http://ww1.sinaimg.cn/large/83029c1egy1fni7wwoow5j20dw099jsf.jpg", "http://ww1.sinaimg.cn/large/83029c1egy1fni7fmpvx9j20dw09raam.jpg");
        Recommend recommend = new Recommend();
        recommend.goodsUrl = "http://ww1.sinaimg.cn/large/83029c1egy1fni7wwcrrdj20go0ciq4x.jpg";
        recommend.price = "￥24.88";
        recommend.name = "大金桔店";
        for (int i = 0; i < 6; i++) {
            recommendList.add(recommend);
        }
    }

    private BGABanner.Adapter bannerAdapter = new BGABanner.Adapter() {
        @Override
        public void fillBannerItem(BGABanner banner, View itemView, @Nullable Object model, int position) {
            Glide.with(getContext())
                    .load(model)
                    .apply(new RequestOptions().placeholder(R.drawable.holder).error(R.drawable.holder).dontAnimate().centerCrop())
                    .into((ImageView) itemView);
        }
    };

    private BGABanner.Delegate delegate = new BGABanner.Delegate() {
        @Override
        public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

        }
    };


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onUnauthorized() {

    }

    @Override
    public void onLoadRecommendSuccess(List<Recommend> recommendList) {
        if (recommendList != null) {
            adapter.setData(recommendList);
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onLoadRecommendFailure(String msg) {
        showShortToast(msg);
    }
}
