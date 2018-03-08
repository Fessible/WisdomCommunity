package com.example.com.wisdomcommunity.ui.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;

/**
 * Created by rhm on 2018/3/8.
 */

public class SearchFragment extends BaseFragment {
    public static final String TAG_SEARCH_FRAGMENT = "SEARCH_FRAGMENT";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_view)
    EditText searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SearchAdapter searchAdapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationIcon(R.drawable.selector_btn_back);
        toolbar.setNavigationOnClickListener(onNavigationListener);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build())
                .build());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(searchAdapter);
        searchView.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnClickListener onNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };

    @Override
    protected void destroyView() {

    }
}
