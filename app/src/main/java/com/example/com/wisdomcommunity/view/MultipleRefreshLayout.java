package com.example.com.wisdomcommunity.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.example.com.wisdomcommunity.R;

import java.security.InvalidParameterException;

@SuppressWarnings("deprecation")
public class MultipleRefreshLayout extends SwipeRefreshLayout implements MultipleStrip {

    @LayoutRes
    private int emptyLayoutId;
    private View emptyView;

    @LayoutRes
    private int errorLayoutId;
    private View errorView;

    @LayoutRes
    private int loadingLayoutId;
    private View loadingView;

    public MultipleRefreshLayout(Context context) {
        this(context, null);
    }

    public MultipleRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context, attrs);
    }

    private void setup(Context context, AttributeSet attrs) {
        setOnHierarchyChangeListener(onHierarchyChangeListener);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Multiple);
        loadingLayoutId = ta.getResourceId(R.styleable.Multiple_loadingView, 0);
        emptyLayoutId = ta.getResourceId(R.styleable.Multiple_emptyView, 0);
        errorLayoutId = ta.getResourceId(R.styleable.Multiple_errorView, 0);
        boolean inflaterImmediate = ta.getBoolean(R.styleable.Multiple_inflaterImmediate, false);
        if (inflaterImmediate) {
            if (loadingLayoutId != 0) {
                loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, this, false);
                addView(loadingView);
                loadingView.setVisibility(View.GONE);
            }
            if (emptyLayoutId != 0) {
                emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, this, false);
                addView(emptyView);
                emptyView.setVisibility(View.GONE);
            }
            if (errorLayoutId != 0) {
                errorView = LayoutInflater.from(getContext()).inflate(errorLayoutId, this, false);
                addView(errorView);
                errorView.setVisibility(View.GONE);
            }
        }
        ta.recycle();
    }

    private OnHierarchyChangeListener onHierarchyChangeListener = new OnHierarchyChangeListener() {
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (loadingView != null) {
                loadingView.bringToFront();
            }
            if (emptyView != null) {
                emptyView.bringToFront();
            }
            if (errorView != null) {
                errorView.bringToFront();
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (child == emptyView) {
                emptyView = null;
            }
            if (child == errorView) {
                errorView = null;
            }
            if (child == loadingView) {
                loadingView = null;
            }
        }
    };

    public void setLoadingLayoutId(@LayoutRes int loadingLayoutId) {
        if (this.loadingLayoutId != loadingLayoutId) {
            removeLoadingViewIfNeed();
            this.loadingLayoutId = loadingLayoutId;
        }
    }

    private void removeLoadingViewIfNeed() {
        if (loadingView != null) {
            removeView(loadingView);
            loadingView = null;
        }
    }

    public void setEmptyLayoutId(@LayoutRes int emptyLayoutId) {
        if (this.emptyLayoutId != emptyLayoutId) {
            removeEmptyViewIfNeed();
            this.emptyLayoutId = emptyLayoutId;
        }
    }

    private void removeEmptyViewIfNeed() {
        if (emptyView != null) {
            removeView(emptyView);
            emptyView = null;
        }
    }

    public void setErrorLayoutId(@LayoutRes int errorLayoutId) {
        if (this.errorLayoutId != errorLayoutId) {
            removeErrorViewIfNeed();
            this.errorLayoutId = errorLayoutId;
        }
    }

    private void removeErrorViewIfNeed() {
        if (errorView != null) {
            removeView(errorView);
            errorView = null;
        }
    }

    @Override
    public void showLoading(boolean preventTouch) {
        if (loadingView == null) {
            if (loadingLayoutId == 0) {
                throw new InvalidParameterException("Loading View LayoutId is invalid");
            }
            loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, this, false);
            addView(loadingView);
        }
        loadingView.setOnTouchListener(preventTouch ? onPreventTouchListener : null);
        loadingView.setVisibility(View.VISIBLE);
    }

    private OnTouchListener onPreventTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    @Override
    public void hideLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isLoading() {
        return loadingView != null && loadingView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showEmpty() {
        if (emptyView == null) {
            if (emptyLayoutId == 0) {
                throw new InvalidParameterException("Empty View LayoutId is invalid");
            }
            emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, this, false);
            addView(emptyView);
        }
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isEmpty() {
        return emptyView != null && emptyView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showError() {
        if (errorView == null) {
            if (errorLayoutId == 0) {
                throw new InvalidParameterException("Error View LayoutId is invalid");
            }
            errorView = LayoutInflater.from(getContext()).inflate(errorLayoutId, this, false);
            addView(errorView);
        }
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isError() {
        return errorView != null && errorView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showLoadingOnly(boolean preventTouch) {
        if (isEmpty()) {
            hideEmpty();
        }
        if (isError()) {
            hideError();
        }
        if (!isLoading()) {
            showLoading(preventTouch);
        }
    }

    @Override
    public void showEmptyOnly() {
        if (isLoading()) {
            hideLoading();
        }
        if (isError()) {
            hideError();
        }
        if (!isEmpty()) {
            showEmpty();
        }
    }

    @Override
    public void showErrorOnly() {
        if (isLoading()) {
            hideLoading();
        }
        if (isEmpty()) {
            hideEmpty();
        }
        if (!isError()) {
            showError();
        }
    }

    @Override
    public void showContentOnly() {
        if (isLoading()) {
            hideLoading();
        }
        if (isEmpty()) {
            hideEmpty();
        }
        if (isError()) {
            hideError();
        }
    }
}
