package com.example.com.wisdomcommunity.ui.order;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.support_business.Constants;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_ADDRESS;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_HEADER_SHOP;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_ORDER_LIST;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_PHONE;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_PRICE;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/3/5.
 */

public class OrderDetailAdapter extends BaseAdapter<OrderDetailAdapter.DetailHolder> {
    private final List<Item> itemList = new ArrayList<>();
    private Context context;
    private OrderDetail detail;
    private final SimpleDateFormat simpleDateFormat;
    private Callback callback;

    public OrderDetailAdapter(Context context) {
        simpleDateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.SIMPLIFIED_CHINESE);
        registerAdapterDataObserver(observer);
        this.context = context;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setData(OrderDetail detail) {
        this.detail = detail;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            if (detail != null) {
                itemList.add(new EmptyItem());
                itemList.add(new HeaderItem(detail.shopName, detail.shopId));
                itemList.add(new PriceItem(detail.total, String.valueOf(detail.shipment), detail.pay));
                List<OrderDetail.Order> orderList = detail.details;
                if (orderList != null && !orderList.isEmpty()) {//清单详情
                    for (int i = 0; i < orderList.size(); i++) {
                        OrderDetail.Order order = orderList.get(i);
                        itemList.add(new OrderItem(order));
                    }
                }
                itemList.add(new PhoneItem(detail.shopPhone));
                itemList.add(new EmptyItem());
                itemList.add(new StandardItem(context.getString(R.string.title_order_id), detail.orderId));
                itemList.add(new StandardItem(context.getString(R.string.order_status), getOrderStatus(detail.orderStatus)));
                itemList.add(new StandardItem(context.getString(R.string.order_time), simpleDateFormat.format(new Date(detail.orderTime))));
                itemList.add(new StandardItem(context.getString(R.string.order_receive), detail.receiverName));
                itemList.add(new StandardItem(context.getString(R.string.contact_phone), detail.receiverPhone));
                itemList.add(new EmptyItem());
                itemList.add(new AddressItem(context.getString(R.string.title_address), detail.address));
                itemList.add(new EmptyItem());
                if (detail.remark != null) {
                    itemList.add(new AddressItem(context.getString(R.string.remark), detail.remark));
                }
                itemList.add(new EmptyItem());
            }
        }
    };

    private String getOrderStatus(@Constants.STATUS int detailStatus) {
        String status = null;
        switch (detailStatus) {
            case Constants.STATUS_FINISHED:
                status = context.getString(R.string.status_finished);
                break;
            case Constants.STATUS_WAIT_PAY:
                status = context.getString(R.string.status_wait_pay);
                break;
            case Constants.STATUS_SENDING:
                status = context.getString(R.string.status_sending);
                break;
            case Constants.STATUS_SUBMIT:
                status = context.getString(R.string.status_submit);
                break;
        }
        return status;
    }

    @Override
    protected void destroy() {
        itemList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DetailHolder holder = null;
        switch (viewType) {
            case VIEW_ADDRESS:
                holder = new AddressHolder(context, parent);
                break;
            case VIEW_EMPTY:
                holder = new EmptyHolder(context, parent);
                break;
            case VIEW_STANDARD:
                holder = new StandardHolder(context, parent);
                break;
            case VIEW_HEADER_SHOP:
                holder = new HeaderHolder(context, parent);
                break;
            case VIEW_ORDER_LIST:
                holder = new ListHolder(context, parent);
                break;
            case VIEW_PHONE:
                holder = new PhoneHolder(context, parent);
                break;
            case VIEW_PRICE:
                holder = new PriceHolder(context, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {
        holder.bindHolder(context, itemList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class HeaderHolder extends DetailHolder<HeaderItem> {
        @BindView(R.id.title)
        TextView title;

        public HeaderHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_detail_header);
        }

        @Override
        public void bindHolder(Context context, final HeaderItem item, final Callback callback) {
            title.setText(item.shopName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(item.shopId, item.shopName, VIEW_HEADER_SHOP);
                    }
                }
            });
        }
    }

    static class PriceHolder extends DetailHolder<PriceItem> {
        @BindView(R.id.total_price)
        TextView totalPrice;

        @BindView(R.id.shipment)
        TextView shipment;

        @BindView(R.id.pay)
        TextView pay;

        public PriceHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_detail_price);
        }

        @Override
        public void bindHolder(Context context, PriceItem item, Callback callback) {
            totalPrice.setText(context.getString(R.string.sign_price, item.total));
            shipment.setText(context.getString(R.string.sign_price, item.shipment));
            pay.setText(context.getString(R.string.sign_price, item.pay));
        }
    }

    static class ListHolder extends DetailHolder<OrderItem> {
        @BindView(R.id.number)
        TextView number;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.goods_name)
        TextView goodsName;


        public ListHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_detail_list);
        }

        @Override
        public void bindHolder(Context context, OrderItem item, Callback callback) {
            OrderDetail.Order order = item.order;
            number.setText(context.getString(R.string.sign_number, order.number));
            price.setText(context.getString(R.string.sign_price, order.price));
            goodsName.setText(order.goodsName);
        }
    }

    static class PhoneHolder extends DetailHolder<PhoneItem> {

        public PhoneHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_detail_phone);
        }

        @Override
        public void bindHolder(Context context, final PhoneItem item, final Callback callback) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(item.shopPhone, "", VIEW_PHONE);
                    }
                }
            });
        }
    }

    static class AddressHolder extends DetailHolder<AddressItem> {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.content)
        TextView content;

        public AddressHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_detail_address);
        }

        @Override
        public void bindHolder(Context context, AddressItem item, Callback callback) {
            title.setText(item.title);
            content.setText(item.content);
        }
    }

    static class StandardHolder extends DetailHolder<StandardItem> {
        @BindView(R.id.alias)
        TextView alias;

        @BindView(R.id.content)
        TextView content;

        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_order_detail_standard);
        }

        @Override
        public void bindHolder(Context context, StandardItem item, Callback callback) {
            alias.setText(item.title);
            content.setText(item.desc);
        }
    }

    static class EmptyHolder extends DetailHolder<EmptyItem> {
        public EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_empty);
        }

    }


    static class AddressItem implements Item {
        private String title;
        private String content;

        public AddressItem(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public int getViewType() {
            return VIEW_ADDRESS;
        }
    }

    static class StandardItem implements Item {
        private String title;
        private String desc;

        public StandardItem(String title, String desc) {
            this.title = title;
            this.desc = desc;
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

    static class PhoneItem implements Item {
        private String shopPhone;

        public PhoneItem(String shopPhone) {
            this.shopPhone = shopPhone;
        }

        @Override
        public int getViewType() {
            return VIEW_PHONE;
        }
    }

    static class HeaderItem implements Item {
        private String shopName;
        private String shopId;

        public HeaderItem(String shopName, String shopId) {
            this.shopName = shopName;
            this.shopId = shopId;
        }

        @Override
        public int getViewType() {
            return VIEW_HEADER_SHOP;
        }
    }

    static class PriceItem implements Item {
        private String total;
        private String shipment;
        private String pay;

        public PriceItem(String total, String shipment, String pay) {
            this.total = total;
            this.shipment = shipment;
            this.pay = pay;
        }

        @Override
        public int getViewType() {
            return VIEW_PRICE;
        }
    }

    static class OrderItem implements Item {
        private OrderDetail.Order order;

        public OrderItem(OrderDetail.Order order) {
            this.order = order;
        }

        @Override
        public int getViewType() {
            return VIEW_ORDER_LIST;
        }
    }

    public interface Item {
        int VIEW_ADDRESS = 0;
        int VIEW_STANDARD = 1;
        int VIEW_EMPTY = 2;
        int VIEW_PHONE = 3;//电话
        int VIEW_HEADER_SHOP = 4;//店铺名称
        int VIEW_PRICE = 5;//订单总额
        int VIEW_ORDER_LIST = 6;//清单

        @IntDef({
                VIEW_ADDRESS,
                VIEW_STANDARD,
                VIEW_EMPTY,
                VIEW_PHONE,
                VIEW_HEADER_SHOP,
                VIEW_PRICE,
                VIEW_ORDER_LIST
        })
        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    static abstract class DetailHolder<II extends Item> extends BaseHolder {
        public DetailHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public void bindHolder(Context context, II item, Callback callback) {
        }
    }

    interface Callback {
        void onCallback(String value, String name, int type);
    }

}
