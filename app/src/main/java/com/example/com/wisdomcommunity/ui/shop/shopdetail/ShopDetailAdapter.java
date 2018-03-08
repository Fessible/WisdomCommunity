package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rhm on 2018/3/6.
 */

public class ShopDetailAdapter extends BaseAdapter<ShopDetailAdapter.ShopDetailHolder> {
    private Context context;
    private List<Goods> goodsList = new ArrayList<>();
    private Callback callback;

    public ShopDetailAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void destroy() {
        goodsList.clear();
    }

    @Override
    public ShopDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopDetailHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(ShopDetailHolder holder, int position) {
        holder.bindHolder(context, goodsList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    static class ShopDetailHolder extends BaseHolder {
        @BindView(R.id.icon_goods)
        ImageView iconGoods;

        @BindView(R.id.goods_name)
        TextView goodsName;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.number)
        TextView number;

        @BindView(R.id.add)
        ImageView add;

        @BindView(R.id.minus)
        ImageView minus;

        @BindView(R.id.content_layout)
        RelativeLayout contentLayout;

        private int count;
        private double pay;

        public ShopDetailHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shop_goods);
        }

        public void bindHolder(Context context, final Goods item, final Callback callback) {
            int placeHolder = R.drawable.app_icon;
            Glide.with(context).load(item.goodsUrl)
                    .apply(new RequestOptions()
                            .fallback(placeHolder)
                            .placeholder(placeHolder)
                            .centerCrop())
                    .into(iconGoods);
            goodsName.setText(item.goodsName);

            contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(item.goodsName, item.goodsId);
                    }
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number.setText(String.valueOf(count++));
                    pay = Double.valueOf(item.price) * count;
                    if (callback != null) {
                        callback.onPayBack(pay);
                    }
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number.setText(String.valueOf(count--));
                    pay = Double.valueOf(item.price) * count;
                    if (callback != null) {
                        callback.onPayBack(pay);
                    }
                }
            });
        }
    }


    interface Callback {
        void onCallback(String name, String goodsID);

        void onPayBack(double price);
    }

}
