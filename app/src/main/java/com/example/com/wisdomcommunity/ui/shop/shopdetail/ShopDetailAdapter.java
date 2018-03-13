package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.text.DecimalFormat;
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
    private int number;

    ShopDetailAdapter(Context context) {
        this.context = context;
    }

    void setNum(int num, int position) {
        number = num;
        notifyItemChanged(position);
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
        holder.bindHolder(context, goodsList.get(position), number, position, callback);
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

        int count;

        ShopDetailHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shop_goods);
        }

        public void bindHolder(final Context context, final Goods item, int num, final int position, final Callback callback) {
            int placeHolder = R.drawable.app_icon;
            final Float flPrice = Float.valueOf(item.price);
            count = num;
            number.setText(String.valueOf(count));
            if (count > 0) {
                changMinus(true);
            }
            Glide.with(context).load(item.goodsUrl)
                    .apply(new RequestOptions()
                            .fallback(placeHolder)
                            .placeholder(placeHolder)
                            .centerCrop())
                    .into(iconGoods);
            goodsName.setText(item.goodsName);
            price.setText(context.getString(R.string.sign_price, item.price));

            contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(item, count, position);
                    }
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (count < item.remain) {
                        number.setText(String.valueOf(++count));
                        if (count > 0) {
                            minus.setBackgroundResource(R.drawable.icon_minus_n);
                        }
                        if (callback != null) {
                            callback.onAddPayBack(item, flPrice, count);
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.no_enough_remain), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count > 0) {
                        changMinus(true);
                        number.setText(String.valueOf(--count));
                        if (count <= 0) {
                            changMinus(false);
                        }
                        if (callback != null) {
                            callback.onMinusPayBack(item, flPrice, count);
                        }
                    } else {
                        changMinus(false);
                    }
                }
            });
        }

        private void changMinus(boolean isEnable) {
            minus.setBackgroundResource(isEnable ? R.drawable.icon_minus_n : R.drawable.icon_minus_un);
        }
    }


    interface Callback {
        void onCallback(Goods goods, int count, int position);

        void onAddPayBack(Goods goods, float price, int num);

        void onMinusPayBack(Goods goods, float price, int num);
    }

}
