package com.example.com.wisdomcommunity.ui.person.set;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by rhm on 2018/4/15.
 */
public class AboutFragment extends BaseFragment {
    public static final String TAG_ABOUT_FRAGMENT = "ABOUT_FRAGMENT";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getResLayout() {
        return R.layout.fragment_set_about;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected void destroyView() {

    }
}
