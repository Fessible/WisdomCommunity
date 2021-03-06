package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;
import com.example.com.wisdomcommunity.ui.shop.cart.CartAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rhm on 2018/3/19.
 */

public class DialogCartAdapter extends BaseAdapter<DialogCartAdapter.CartHolder> {
    private Context context;
    private List<OrderDetail.Order> orderList = new ArrayList<>();
    private DialogCartAdapter.Callback callback;

    public DialogCartAdapter(Context context) {
        this.context = context;
    }

    public void setCallback(DialogCartAdapter.Callback callback) {
        this.callback = callback;
    }

    void setOrderData(List<OrderDetail.Order> orders) {
        orderList = orders;
    }

    @Override
    protected void destroy() {
        if (orderList != null) {
            orderList.clear();
        }
    }

    public List<OrderDetail.Order> getOrderList() {
        return orderList;
    }

    void remove(int position) {
        orderList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public DialogCartAdapter.CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogCartAdapter.CartHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(DialogCartAdapter.CartHolder holder, int position) {
        holder.bindHolder(context, orderList.get(position), position, callback);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class CartHolder extends BaseHolder {

        @BindView(R.id.goods_name)
        TextView goodsName;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.minus)
        ImageView minus;

        @BindView(R.id.add)
        ImageView add;

        @BindView(R.id.number)
        TextView number;

        private int count;

        CartHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_dialog_cart);
        }

        public void bindHolder(final Context context, final OrderDetail.Order order, final int position, final DialogCartAdapter.Callback callback) {

            goodsName.setText(order.goodsName);
            price.setText(context.getString(R.string.sign_price, order.price));
            number.setText(String.valueOf(order.number));
            count = order.number;
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count < order.remain) {
                        number.setText(String.valueOf(++count));
                        order.number = count;
                        callback.onAddItem(order, Float.valueOf(order.price), count);
                    } else {
                        Toast.makeText(context, context.getString(R.string.no_enough_remain), Toast.LENGTH_LONG).show();
                    }
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count > 1) {
                        number.setText(String.valueOf(--count));
                        order.number = count;
                        if (callback != null) {
                            callback.onMinusItem(order, Float.valueOf(order.price), count);
                        }
                    } else {
                        //delete
                        if (callback != null) {
                            callback.onDelete(position, order, Float.valueOf(order.price));
                        }
                    }
                }
            });
        }
    }

    interface Callback {
        void onDelete(int position, OrderDetail.Order order, Float aFloat);

        void onAddItem(OrderDetail.Order order, float price, int num);

        void onMinusItem(OrderDetail.Order order, float price, int num);
    }


}
