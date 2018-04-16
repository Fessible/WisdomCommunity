package com.example.com.wisdomcommunity.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.com.wisdomcommunity.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础Fragment
 * Created by rhm on 2018/1/16.
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResLayout(), container, false);
    }

    @LayoutRes
    public abstract int getResLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
    }

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected void showShortToast(String msg) {
        ToastUtils.showToast(getActivity(),msg);
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected abstract void destroyView();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        destroyView();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
