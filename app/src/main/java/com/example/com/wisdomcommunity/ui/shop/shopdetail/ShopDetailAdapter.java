package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
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
import com.example.com.wisdomcommunity.localsave.ShopCart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/3/6.
 */

public class ShopDetailAdapter extends BaseAdapter<ShopDetailAdapter.ShopDetailHolder> {
    private Context context;
    private List<Goods> goodsList = new ArrayList<>();
    private final List<Item> itemList = new ArrayList<>();

    private Callback callback;
    private int number = -1;
    private int type;
    private String shopId;

    ShopDetailAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(observer);
    }

    void setShopId(String shopId) {
        this.shopId = shopId;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            if (goodsList != null && !goodsList.isEmpty()) {
                for (Goods goods : goodsList) {
                    itemList.add(new StandardItem(goods, shopId));
                }
            } else {
                itemList.add(new EmptyItem());
            }
        }
    };


    void setNum(int num, int position) {
        number = num;
        notifyItemChanged(position);
    }

    void setNum(int num) {
        number = num;
    }

    public void setData(List<Goods> goodsList, int type) {
        number = -1;
        this.goodsList = goodsList;
        this.type = type;
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void destroy() {
        goodsList.clear();
        unregisterAdapterDataObserver(observer);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public ShopDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ShopDetailHolder holder = null;
        switch (viewType) {
            case VIEW_STANDARD:
                holder = new StandardHolder(context, parent);
                break;
            case VIEW_EMPTY:
                holder = new EmptyHolder(context, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ShopDetailHolder holder, int position) {
        holder.bindHolder(context, itemList.get(position), number, position, type, callback);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class EmptyHolder extends ShopDetailHolder {

        EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shopdetail_goods_empty);
        }
    }

    static class StandardHolder extends ShopDetailHolder<StandardItem> {

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

        StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shop_goods);
        }

        @Override
        public void bindHolder(final Context context, final StandardItem standardItem, int num, final int position, final int type, final Callback callback) {
            super.bindHolder(context, standardItem, num, position, type, callback);
            final Goods item = standardItem.goods;
            item.type = type;
            final String shopId = standardItem.shopId;
            final String localShopId = ShopCart.getShopId(context);

            int placeHolder = R.drawable.app_icon;
            final Float flPrice = Float.valueOf(item.price);
            if (num != -1) {
                count = num;
            } else {
                count = item.num;
            }

            if (count > 0) {
                changMinus(true);
            } else {
                changMinus(false);
            }
            number.setText(String.valueOf(count));
            Glide.with(context).load(item.goodsUrl)
                    .apply(new RequestOptions()
                            .fallback(placeHolder)
                            .placeholder(placeHolder)
                            .centerCrop())
                    .into(iconGoods);
            goodsName.setText(item.goodsName);
            price.setText(context.getString(R.string.sign_price, item.price));
//            price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加斜线

            contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(item, count, position, type);
                    }
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopId != null && ShopCart.getShopId(context) != null &&!ShopCart.getShopId(context).isEmpty()&& !shopId.equals(localShopId)) {
                        //提示是否清空购物车
                        if (callback != null) {
                            callback.onClearShopCart();
                            return;
                        }
                    }

                    if (count < item.remain) {
                        number.setText(String.valueOf(++count));
                        if (count > 0) {
                            changMinus(true);
                        }
                        if (callback != null) {
                            callback.onAddPayBack(v, item, flPrice, count, type);
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
                            callback.onMinusPayBack(item, flPrice, count, type);
                        }
                    } else {
                        changMinus(false);
                    }
                }
            });
        }


        private void changMinus(boolean isEnable) {
//            minus.setBackgroundResource(isEnable ? R.drawable.icon_minus_n : R.drawable.icon_minus_un);
            minus.setVisibility(isEnable ? View.VISIBLE : View.GONE);
            number.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        }
    }

    abstract static class ShopDetailHolder<II extends Item> extends BaseHolder {

        ShopDetailHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public void bindHolder(final Context context, II item, int num, final int position, final int type, final Callback callback) {
        }
    }

    static class EmptyItem implements Item {

        @Override
        public int getViewType() {
            return VIEW_EMPTY;
        }
    }

    static class StandardItem implements Item {
        private Goods goods;
        private String shopId;

        public StandardItem(Goods goods, String shopId) {
            this.goods = goods;
            this.shopId = shopId;
        }

        @Override
        public int getViewType() {
            return VIEW_STANDARD;
        }
    }

    interface Item {
        final static int VIEW_EMPTY = 0;
        final static int VIEW_STANDARD = 1;

        @IntDef({
                VIEW_EMPTY,
                VIEW_STANDARD
        })
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    interface Callback {
        void onCallback(Goods goods, int count, int position, int type);

        void onAddPayBack(View v, Goods goods, float price, int num, int type);

        void onMinusPayBack(Goods goods, float price, int num, int type);

        void onClearShopCart();
    }

}
