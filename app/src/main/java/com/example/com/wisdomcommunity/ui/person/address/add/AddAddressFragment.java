package com.example.com.wisdomcommunity.ui.person.address.add;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.TITLE;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddAddressFragment extends BaseFragment {
    public static final String TAG_ADD_ADDRESS_FRAGMENT = "ADD_ADDRESS_FRAGMENT";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddAddressAdapter addAddressAdapter;
    private String strTitle;

    @Override
    public int getResLayout() {
        return R.layout.fragment_add_address;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            strTitle = bundle.getString(TITLE);
        }
        title.setText(strTitle);
        addAddressAdapter = new AddAddressAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(addAddressAdapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        addAddressAdapter.setOnItemClickListener(new AddAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                //选择地址
            }
        });
        addAddressAdapter.notifyDataSetChanged();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.save)
    public void save() {
    }

    @Override
    protected void destroyView() {
        addAddressAdapter.destroy();
    }
}
