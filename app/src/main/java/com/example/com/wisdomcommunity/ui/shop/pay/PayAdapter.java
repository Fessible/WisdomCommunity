package com.example.com.wisdomcommunity.ui.shop.pay;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    private List<OrderDetail.Order> orderList = new ArrayList<>();
    private Context context;
    private int shipment;
    private String totalPay;

    PayAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(observer);
    }

    public void setData(List<OrderDetail.Order> orderList, int shipment, String totalPay) {
        this.orderList = orderList;
        this.shipment = shipment;
        this.totalPay = totalPay;
    }

    public void setRemark(String remark, int position) {
        RemarkItem remarkItem = (RemarkItem) itemList.get(position);
        remarkItem.remark = remark;
        notifyItemChanged(position);
    }

    public void setAddress(String title, String content, int position) {
        AddressItem addressItem = (AddressItem) itemList.get(position);
        addressItem.content = content;
        addressItem.title = title;
        notifyItemChanged(position);
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
            if (orderList != null && !orderList.isEmpty()) {
                itemList.add(new OrderItem(orderList, shipment, totalPay));
            }
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
                holder = new BorderHolder(context, parent);
                break;
            case VIEW_ADDRESS:
                holder = new AddressHolder(context, parent);
                break;
            case VIEW_ORDER:
                holder = new OrderHolder(context, parent);
                break;
            case VIEW_REMARK:
                holder = new RemarkHolder(context, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(PayHolder holder, int position) {
        holder.bindHolder(context, itemList.get(position), position, callback);
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
        @BindView(R.id.content)
        TextView content;

        RemarkHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_remark);
        }

        @Override
        void bindHolder(Context context, RemarkItem item, final int position, final Callback callback) {
            super.bindHolder(context, item, position, callback);
            if (!TextUtils.isEmpty(item.remark)) {
                content.setText(item.remark);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.remarkItem(position);
                    }
                }
            });
        }
    }

    static class AddressHolder extends PayHolder<AddressItem> {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.content)
        TextView content;

        AddressHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_select_address);
        }

        @Override
        void bindHolder(Context context, AddressItem item, final int position, final Callback callback) {
            super.bindHolder(context, item, position, callback);

            if (!TextUtils.isEmpty(item.title) && !TextUtils.isEmpty(item.content)) {
                title.setText(item.title);
                content.setVisibility(View.VISIBLE);
                content.setText(item.content);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.addressItem(position);
                    }
                }
            });
        }
    }

    static class BorderHolder extends PayHolder<BorderItem> {
        BorderHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_empty);
        }
    }

    static class OrderHolder extends PayHolder<OrderItem> {
        @BindView(R.id.order_layout)
        LinearLayout orderLayout;

        @BindView(R.id.shipment)
        TextView shipment;

        @BindView(R.id.total_price)
        TextView totalPrice;

        OrderHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_order_list);
        }

        @Override
        void bindHolder(Context context, OrderItem item, int position, Callback callback) {
            super.bindHolder(context, item, position, callback);
            shipment.setText(context.getString(R.string.sign_price, String.valueOf(item.shipment)));
            totalPrice.setText(context.getString(R.string.sign_price, item.totalPay));
            orderLayout.removeAllViews();

            if (item.orderList != null && !item.orderList.isEmpty()) {
                for (OrderDetail.Order order : item.orderList) {
                    if (order != null) {
                        View view = LayoutInflater.from(context).inflate(R.layout.item_order_list, orderLayout, false);
                        TextView goodsName = view.findViewById(R.id.goods_name);
                        TextView number = view.findViewById(R.id.number);
                        TextView price = view.findViewById(R.id.price);

                        goodsName.setText(order.goodsName);
                        number.setText(context.getString(R.string.plus_number, order.number));
                        price.setText(context.getString(R.string.sign_price, order.price));
                        orderLayout.addView(view);
                    }
                }
            }

        }
    }

    static class RemarkItem implements Item {
        private String remark;

        public RemarkItem() {
        }

        public RemarkItem(String remark) {
            this.remark = remark;
        }

        @Override
        public int getViewType() {
            return VIEW_REMARK;
        }
    }

    static class AddressItem implements Item {
        private String title;
        private String content;

        public AddressItem() {

        }

        public AddressItem(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public int getViewType() {
            return VIEW_ADDRESS;
        }
    }

    static class OrderItem implements Item {

        private List<OrderDetail.Order> orderList;
        private int shipment;
        private String totalPay;

        OrderItem(List<OrderDetail.Order> orderList, int shipment, String totalPay) {
            this.orderList = orderList;
            this.shipment = shipment;
            this.totalPay = totalPay;
        }

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

        void bindHolder(Context context, II item, int position, Callback callback) {

        }
    }

    interface Callback {
        void remarkItem(int position);

        void addressItem(int position);
    }
}
