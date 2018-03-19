package com.example.com.wisdomcommunity.ui.splash;

import android.os.Bundle;
import android.view.View;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

/**
 * Created by rhm on 2018/3/19.
 */

public class SplashFragment extends BaseFragment {

    public static final String TAG_SPLASH_FRAGMENT = "SPLASH_FRAGMENT";

    @Override
    public int getResLayout() {
        return R.layout.fragment_guide_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void destroyView() {

    }
}
