package com.example.com.wisdomcommunity.ui.shop;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;
import com.example.com.wisdomcommunity.ui.home.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_HEADER;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/1/17.
 */

public class ShopAdapter extends BaseAdapter<ShopAdapter.ShopHolder> {

    private List<ShopList> shopLists = new ArrayList<>();
    private final List<Item> itemList = new ArrayList<>();

    private Context mContext;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public ShopAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            itemList.add(new EmptyItem());
            if (shopLists != null && !shopLists.isEmpty()) {
                for (ShopList shopList : shopLists) {
                    itemList.add(new HeaderItem(shopList.shopName, shopList.shopId, shopList.shopUrl));
                    List<Goods> goodsList = shopList.goodsList;
                    if (goodsList != null && !goodsList.isEmpty()) {
                        for (int i = 0; i < goodsList.size(); i++) {
                            itemList.add(new StandardItem(goodsList.get(i)));
                        }
                    }
                    itemList.add(new EmptyItem());
                }
            }
        }
    };

    public void setData(List<ShopList> shopLists) {
        this.shopLists = shopLists;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public ShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShopHolder holder = null;
        switch (viewType) {
            case VIEW_HEADER:
                holder = new HeadHolder(mContext, parent);
                break;
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
    public void onBindViewHolder(ShopHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    protected void destroy() {
        itemList.clear();
        if (shopLists != null) {
            shopLists.clear();
        }
    }

    public class HeadHolder extends ShopHolder<HeaderItem> {
        @BindView(R.id.shop_icon)
        ImageView shopIcon;

        @BindView(R.id.shop_name)
        TextView shopName;

        @BindView(R.id.enter)
        TextView enter;

        public HeadHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shop_header);
        }

        @Override
        void bindHolder(Context context, final HeaderItem item, final Callback callback) {
            final int placeHolderId = R.drawable.app_icon;
            Glide.with(context).load(item.shopUrl)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(placeHolderId)
                            .fallback(placeHolderId))
                    .into(shopIcon);
            shopName.setText(item.shopName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(item.shopId, VIEW_HEADER);
                    }
                }
            });
        }
    }

    public class StandardHolder extends ShopHolder<StandardItem> {
        @BindView(R.id.goods_name)
        TextView goodsName;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.standard)
        TextView standard;

        @BindView(R.id.sale)
        TextView sale;

        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shop_standard);
        }

        @Override
        void bindHolder(Context context, StandardItem item, final Callback callback) {
            final Goods goods = item.goods;
            goodsName.setText(goods.goodsName);
            price.setText(goods.price);
            standard.setText(context.getString(R.string.standard,goods.standard));
            sale.setText(context.getString(R.string.already_sale,goods.sale));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCallback(goods.goodsId,VIEW_STANDARD);
                    }
                }
            });
        }
    }

    public class EmptyHolder extends ShopHolder<EmptyItem> {

        public EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_empty);
        }

        @Override
        void bindHolder(Context context, EmptyItem item, Callback callback) {

        }
    }

    public class StandardItem implements Item {

        private Goods goods;

        public StandardItem(Goods goods) {
            this.goods = goods;
        }

        @Override
        public int getViewType() {
            return VIEW_STANDARD;
        }
    }

    public class HeaderItem implements Item {
        private String shopUrl;
        private String shopName;
        private String shopId;

        public HeaderItem(String shopName, String shopId, String shopUrl) {
            this.shopName = shopName;
            this.shopId = shopId;
            this.shopUrl = shopUrl;
        }

        @Override
        public int getViewType() {
            return VIEW_HEADER;
        }
    }

    public class EmptyItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_EMPTY;
        }
    }


    public interface Item {

        int VIEW_HEADER = 0;
        int VIEW_STANDARD = 1;
        int VIEW_EMPTY = 3;

        @IntDef({
                VIEW_HEADER, VIEW_STANDARD, VIEW_EMPTY
        })
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    public abstract class ShopHolder<II extends Item> extends BaseHolder {
        public ShopHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        abstract void bindHolder(Context context, II item, Callback callback);
    }

    interface Callback {
        void onCallback(String value, int type);
    }

}
