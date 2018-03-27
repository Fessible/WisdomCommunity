package com.example.com.wisdomcommunity.ui.person.set;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.personal.Version;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.SetContract;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_ABOUT;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_CACHE;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_SHARE;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_UPDATE;

/**
 * Created by rhm on 2018/2/28.
 */

public class SetFragment extends BaseFragment implements SetContract.View {
    public static final String TAG_SET_FRAGMENT = "SET_FRAGMENT";
    private SetAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.title)
    TextView title;

    private SetContract.Presenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_template_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter = new SetPresenter(getContext(), SetFragment.this);

        title.setText(getContext().getString(R.string.setting));
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SetAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int type) {
                switch (type) {
                    case TYPE_SHARE:
                        break;
                    case TYPE_ABOUT:
                        break;
                    case TYPE_CACHE:
                        break;
                    case TYPE_UPDATE://版本更新
                        if (presenter != null) {
                            presenter.version(getVersionCode(getContext()));
                        }
                        break;
                }

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 获取版本
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    protected void destroyView() {

    }

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
    public void versionSuccess(final Version version, String msg) {
        if (version != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("版本更新")
                    .setMessage(version.directions)
                    .setPositiveButton(R.string.opt_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), UpdateService.class);
                            intent.putExtra("apkUrl", version.latestVerUrl);
                            getActivity().startService(intent);
                        }
                    })
                    .setNegativeButton(R.string.opt_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            //版本已经是最新
            showShortToast(msg);
        }
    }

    @Override
    public void versionFailure(String msg) {
        showShortToast(msg);
    }
}
