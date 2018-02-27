package com.example.com.wisdomcommunity.ui.person;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.person.address.AddressFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_ADDRESS;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_FEEDBACK;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_SERVICE;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_SET;

/**
 * 个人中心
 * Created by rhm on 2018/1/16.
 */

public class PersonFragment extends BaseFragment {
    public static final String TAG_PERSON_FRAGMENT = "PERSON_FRAGMENT";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    PersonAdapter adapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_person_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
                        .build()).build());
        adapter.setOnItemClickListener(new PersonAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int type) {
                switch (type) {
                    case TYPE_ADDRESS:
                        IntentUtil.startTemplateActivity(PersonFragment.this, AddressFragment.class, AddressFragment.TAG_ADDRESS_FRAGMENT);
                        break;
                    case TYPE_FEEDBACK:
                        break;
                    case TYPE_SERVICE:
                        break;
                    case TYPE_SET:
                        break;
                }
            }
        });
    }

    @Override
    protected void destroyView() {
        adapter.destroy();
    }
}
