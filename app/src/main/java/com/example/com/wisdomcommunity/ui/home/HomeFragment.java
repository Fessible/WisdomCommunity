package com.example.com.wisdomcommunity.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 主页
 * Created by rhm on 2018/1/16.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.banner_guide_content)
    BGABanner banner;

    @Override
    public int getResLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .into(itemView);
            }
        });
        banner.setData(Arrays.asList("http://ww1.sinaimg.cn/large/83029c1egy1fn6vw6n694j205k05k745.jpg","http://ww1.sinaimg.cn/large/83029c1egy1fn6w2phet6j20dw0dwt9a.jpg","http://ww1.sinaimg.cn/large/83029c1egy1fn6w7lq9aoj208c08cmx5.jpg"),Arrays.asList("","",""));
    }
}
