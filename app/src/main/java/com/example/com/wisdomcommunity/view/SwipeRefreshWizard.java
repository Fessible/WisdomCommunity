package com.example.com.wisdomcommunity.view;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;

public class SwipeRefreshWizard extends SwipeRefreshLayout.RefreshWizard {

    private ImageView arrow;
    private ProgressBar wait;
    private TextView text;

    private final int refreshHeight;

    public SwipeRefreshWizard(Context context, SwipeRefreshLayout parent) {
        super(context, parent);

        View header = View.inflate(parent.getContext(), R.layout.refresh_wizard_view, null);
        arrow = (ImageView) header.findViewById(R.id.arrow);
        arrow.setVisibility(View.VISIBLE);
        wait = (ProgressBar) header.findViewById(R.id.wait);
        wait.setVisibility(View.INVISIBLE);
        text = (TextView) header.findViewById(R.id.text);

        measureView(header);

        refreshHeight = header.getMeasuredHeight();

        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? SwipeRefreshLayout.LayoutParams.MATCH_PARENT : SwipeRefreshLayout.LayoutParams.FILL_PARENT,
                SwipeRefreshLayout.LayoutParams.WRAP_CONTENT);
        params.height = refreshHeight;
        params.gravity = Gravity.TOP;
        params.topMargin = -refreshHeight;
        parent.addView(header, params);
    }

    @Override
    public int getRefreshHeight() {
        return refreshHeight;
    }

    @Override
    public void onStateChanged(@SwipeRefreshLayout.State int state) {
        switch (state) {
            case SwipeRefreshLayout.STATE_PULL:
                turnPull();
                break;
            case SwipeRefreshLayout.STATE_RELEASE:
                turnRelease();
                break;
            case SwipeRefreshLayout.STATE_REFRESH:
                startRefresh();
                break;
            case SwipeRefreshLayout.STATE_RESET:
                stopRefresh();
                break;
        }
    }

    @Override
    public void onScrollChanged(int scrollY) {

    }

    private void turnPull() {
        text.setText(R.string.tag_refresh);
    }

    private void turnRelease() {
        text.setText(R.string.tag_release);
    }

    private void startRefresh() {
        arrow.setVisibility(View.INVISIBLE);
        wait.setVisibility(View.VISIBLE);
        text.setText(R.string.tag_loading);
    }

    private void stopRefresh() {
        arrow.setVisibility(View.VISIBLE);
        wait.setVisibility(View.INVISIBLE);
        text.setText(R.string.opt_pull);
    }
}
