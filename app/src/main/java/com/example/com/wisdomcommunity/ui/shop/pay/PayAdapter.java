package com.example.com.wisdomcommunity.ui.shop.pay;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import static com.example.com.wisdomcommunity.ui.shop.pay.PayAdapter.Item.VIEW_ADDRESS;
import static com.example.com.wisdomcommunity.ui.shop.pay.PayAdapter.Item.VIEW_BORDER;
import static com.example.com.wisdomcommunity.ui.shop.pay.PayAdapter.Item.VIEW_ORDER;
import static com.example.com.wisdomcommunity.ui.shop.pay.PayAdapter.Item.VIEW_REMARK;

/**
 * Created by rhm on 2018/3/14.
 */

public class PayAdapter extends BaseAdapter<PayAdapter.PayHolder> {
    private Callback callback;
    private List<Item> itemList = new ArrayList<>();
    private Context context;

    public PayAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            itemList.add(new BorderItem());
            itemList.add(new AddressItem());
            itemList.add(new BorderItem());
            itemList.add(new RemarkItem());
            itemList.add(new BorderItem());
            itemList.add(new OrderItem());
            itemList.add(new BorderItem());
        }
    };

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void destroy() {
        itemList.clear();
        unregisterAdapterDataObserver(observer);
    }

    @Override
    public PayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PayHolder holder = null;
        switch (viewType) {
            case VIEW_BORDER:
                break;
            case VIEW_ADDRESS:
                break;
            case VIEW_ORDER:
                break;
            case VIEW_REMARK:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(PayHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class RemarkHolder extends PayHolder<RemarkItem> {
        public RemarkHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_remark);
        }
    }

    static class AddressHolder extends PayHolder<AddressItem> {
        AddressHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_select_address);
        }
    }

    static class BorderHolder extends PayHolder<BorderItem> {
        BorderHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_empty);
        }
    }

    static class OrderHolder extends PayHolder<OrderItem> {
        OrderHolder(Context context, ViewGroup parent) {
            super(context, parent,R.layout.adapter_order_list);
        }
    }


    static class RemarkItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_REMARK;
        }
    }

    static class AddressItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_ADDRESS;
        }
    }

    static class OrderItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_ORDER;
        }
    }

    static class BorderItem implements Item {

        @Override
        public int getViewType() {
            return VIEW_BORDER;
        }
    }

    interface Item {
        int VIEW_BORDER = 0;
        int VIEW_REMARK = 1;
        int VIEW_ORDER = 2;
        int VIEW_ADDRESS = 3;

        @IntDef({
                VIEW_BORDER,
                VIEW_REMARK,
                VIEW_ORDER,
                VIEW_ADDRESS
        })
        @interface ViewType {
        }

        @ViewType
        int getViewType();

    }

    static abstract class PayHolder<II extends Item> extends BaseHolder {
        PayHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }
    }

    interface Callback {

    }
}
