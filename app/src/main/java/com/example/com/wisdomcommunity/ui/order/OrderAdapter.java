package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.example.com.wisdomcommunity.ui.order.OrderAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.order.OrderAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/2/26.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private final List<Item> itemList = new ArrayList<>();
    private Context mContext;
    private List<OrderRecord> orderRecordList = new ArrayList<>();

    public OrderAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(observer);
    }

    public void setData(List<OrderRecord> orderRecordList) {
        this.orderRecordList = orderRecordList;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (orderRecordList != null) {
                if (!orderRecordList.isEmpty()) {
                    for (int i=0;i<orderRecordList.size();i++) {
                        itemList.add(new StandardItem());
                    }
                } else {
                    itemList.add(new EmptyItem());
                }
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderHolder holder = null;
        switch (viewType) {
            case VIEW_STANDARD:
                holder = new StandardHolder(mContext, parent);
                break;
            case VIEW_EMPTY:
                holder = new EmptyHolder(mContext, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class StandardHolder extends OrderHolder<StandardItem> {
        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_order_item);
        }

        @Override
        public void bindHolder(Context context, StandardItem item) {

        }
    }

    class EmptyHolder extends OrderHolder<EmptyItem> {
        public EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_empty);
        }

        @Override
        public void bindHolder(Context context, EmptyItem item) {

        }

    }


    static class StandardItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_STANDARD;
        }
    }

    static class EmptyItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_EMPTY;
        }
    }


    interface Item {
        final static int VIEW_STANDARD = 0;
        final static int VIEW_EMPTY = 1;

        @IntDef({VIEW_STANDARD, VIEW_EMPTY})

        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    abstract class OrderHolder<II extends Item> extends BaseHolder {
        public OrderHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public abstract void bindHolder(Context context, II item);
    }


}
