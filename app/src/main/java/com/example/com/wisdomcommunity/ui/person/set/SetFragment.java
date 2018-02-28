package com.example.com.wisdomcommunity.ui.person.set;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_ABOUT;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_CACHE;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_EXIT;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_SHARE;
import static com.example.com.wisdomcommunity.ui.person.set.SetAdapter.TYPE_UPDATE;

/**
 * Created by rhm on 2018/2/28.
 */

public class SetFragment extends BaseFragment {
    public static final String TAG_SET_FRAGMENT = "SET_FRAGMENT";
    private SetAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public int getResLayout() {
        return R.layout.fragment_set_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
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
                    case TYPE_EXIT:
                        break;
                    case TYPE_UPDATE:
                        break;
                }

            }
        });
    }

    @Override
    protected void destroyView() {

    }

    @OnClick(R.id.back)
    public void back() {

    }
}