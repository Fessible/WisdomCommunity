package com.example.com.wisdomcommunity.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础Activity
 * Created by rhm on 2018/1/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResLayout());
        unbinder = ButterKnife.bind(this);
        onViewCreated(savedInstanceState);
    }

    @LayoutRes
    public abstract int getResLayout();

    protected abstract void onViewCreated(Bundle savedInstanceState);

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 寻找Fragment
     *
     * @param tag
     * @param <F>
     * @return
     */
    public final <F extends Fragment> F findFragment(@NonNull String tag) {
        FragmentManager fm = getSupportFragmentManager();
        return (F) fm.findFragmentByTag(tag);
    }

    /**
     * 添加Fragment
     */
    public final <F extends Fragment> void addFragment(@IdRes int containerViewId, F fragment, @Nullable String tag) {
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(containerViewId, fragment, tag);
            ft.commit();
            fm.executePendingTransactions();//立刻执行
        }
    }

    public final <F extends Fragment> void removeFragmentsIfExits(F... fragments) {
        if (fragments != null && fragments.length > 0) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    ft.remove(fragment);
                }
            }
            ft.commitNowAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }
}
