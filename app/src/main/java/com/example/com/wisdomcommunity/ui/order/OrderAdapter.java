package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;
import com.example.com.wisdomcommunity.util.DateUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.support_business.Constants.STATUS_FINISHED;
import static com.example.com.support_business.Constants.STATUS_SENDING;
import static com.example.com.support_business.Constants.STATUS_WAIT_PAY;
import static com.example.com.wisdomcommunity.ui.order.OrderAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.order.OrderAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/2/26.
 */

public class OrderAdapter extends BaseAdapter<OrderAdapter.OrderHolder> {

    private final List<Item> itemList = new ArrayList<>();
    private Context mContext;
    private List<OrderRecord> orderRecordList = new ArrayList<>();
    private Callback callback;

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
            itemList.clear();
            if (orderRecordList != null) {
                if (!orderRecordList.isEmpty()) {
                    for (int i = 0; i < orderRecordList.size(); i++) {
                        itemList.add(new StandardItem(orderRecordList.get(i)));
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
        holder.bindHolder(mContext, itemList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    protected void destroy() {
        itemList.clear();
    }

    static class StandardHolder extends OrderHolder<StandardItem> {
        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_order_item);
        }

        @BindView(R.id.order_id)
        TextView orderId;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.total_price)
        TextView totalPrice;

        @BindView(R.id.order_time)
        TextView time;

        @Override
        public void bindHolder(Context context, StandardItem item, final Callback callback) {
            final OrderRecord orderRecord = item.getOrderRecord();
            orderId.setText(orderRecord.orderId);
            totalPrice.setText(orderRecord.total);
            status.setText(item.getStatus(context));
            time.setText(item.getTime());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(orderRecord.orderId);
                    }
                }
            });
        }
    }

    static class EmptyHolder extends OrderHolder<EmptyItem> {
        public EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_empty);
        }

        @Override
        public void bindHolder(Context context, EmptyItem item, Callback callback) {
        }

    }


    static class StandardItem implements Item {
        private OrderRecord orderRecord;

        StandardItem(OrderRecord record) {
            this.orderRecord = record;
        }

        public OrderRecord getOrderRecord() {
            return orderRecord;
        }

        public String getTime() {
            return DateUtils.formatYMDHMS(orderRecord.orderTime);
        }

        public String getStatus(Context context) {
            String status = null;
            switch (orderRecord.orderStatus) {
                case STATUS_FINISHED:
                    status = context.getString(R.string.status_finished);
                    break;
                case STATUS_SENDING:
                    status = context.getString(R.string.status_sending);
                    break;
                case STATUS_WAIT_PAY:
                    status = context.getString(R.string.status_wait_pay);
                    break;
            }
            return status;
        }

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

    static abstract class OrderHolder<II extends Item> extends BaseHolder {
        public OrderHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public abstract void bindHolder(Context context, II item, Callback callback);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    interface Callback {
        void onCallback(String value);
    }

}
