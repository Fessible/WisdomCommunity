package com.example.com.wisdomcommunity.ui.person.info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.personal.Info;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.EditInfoContract;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_DISTRICT;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_HEAD_IMAGE;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_NAME;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_SEX;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_SIGNATURE;

/**
 * Created by rhm on 2018/2/28.
 */

public class EditInfoFragment extends BaseFragment implements EditInfoContract.View {
    public static final String TAG_INFO_FRAGMENT = "INFO_FRAGMENT";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private EditInfoAdapter adapter;
    private EditInfoPresenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_edit_person;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter = new EditInfoPresenter(getContext(), EditInfoFragment.this);
        adapter = new EditInfoAdapter(getContext());
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
                        .build()).build());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setClickListener(new EditInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int type) {
                switch (type) {
                    case TYPE_HEAD_IMAGE:
                        break;
                    case TYPE_DISTRICT:
                        break;
                    case TYPE_NAME:
                        break;
                    case TYPE_SEX:
                        break;
                    case TYPE_SIGNATURE:
                        break;
                }
            }
        });
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
    public void loadInfoSuccess(Info info) {
        if (info != null) {
            adapter.setData(info);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInfoFailure(String msg) {

    }
}
