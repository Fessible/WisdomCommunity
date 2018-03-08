package com.example.com.wisdomcommunity.ui.search;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.search.Search;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.search.SearchAdapter.Item.VIEW_BORDER;
import static com.example.com.wisdomcommunity.ui.search.SearchAdapter.Item.VIEW_CONTENT;
import static com.example.com.wisdomcommunity.ui.search.SearchAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.search.SearchAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/3/8.
 */

public class SearchAdapter extends BaseAdapter<SearchAdapter.SearchHolder> {

    private final List<Item> itemList = new ArrayList<>();
    private Context context;
    private Callback callback;
    private String strSearch;
    @Item.ViewType
    private int currentType;
    private List<Search> searchData;

    public SearchAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            switch (currentType) {
                case VIEW_EMPTY:
                    itemList.add(new EmptyItem());
                    break;
                case VIEW_STANDARD:
                    if (!TextUtils.isEmpty(strSearch)) {
                        itemList.add(new StandardItem(strSearch));
                    }
                    break;
                case VIEW_CONTENT:
                    if (searchData != null && !searchData.isEmpty()) {
                        for (Search search : searchData) {
                            if (search != null) {
                                itemList.add(new BorderItem());
                                itemList.add(new ContentItem(search));
                            }
                        }
                        itemList.add(new BorderItem());
                    }
                    break;
            }
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

    public void setStandardData(String search) {
        this.strSearch = search;
        this.currentType = VIEW_STANDARD;
        notifyDataSetChanged();
    }

    public void setEmptyData() {
        itemList.add(new EmptyItem());
        this.currentType = VIEW_EMPTY;
        notifyDataSetChanged();
    }

    public void setData(List<Search> search) {
        this.searchData = search;
        this.currentType = VIEW_CONTENT;
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchHolder holder = null;
        switch (viewType) {
            case VIEW_STANDARD:
                holder = new StandardHoler(context, parent);
                break;
            case VIEW_EMPTY:
                holder = new EmptyHolder(context, parent);
                break;
            case VIEW_CONTENT:
                holder = new ContentHolder(context, parent);
                break;
            case VIEW_BORDER:
                holder = new BorderHolder(context, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        holder.bindHolder(context, itemList.get(position), callback);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class StandardHoler extends SearchHolder<StandardItem> {
        @BindView(R.id.search_content)
        TextView searchContent;

        StandardHoler(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_search_standard);
        }

        @Override
        void bindHolder(Context context, final StandardItem item, final Callback callback) {
            super.bindHolder(context, item, callback);
            searchContent.setText(item.search);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onSearchItem(item.search);
                    }
                }
            });
        }
    }

    static class EmptyHolder extends SearchHolder<EmptyItem> {

        EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_search_empty);
        }
    }

    static class ContentHolder extends SearchHolder<ContentItem> {
        @BindView(R.id.icon)
        ImageView icon;

        @BindView(R.id.name)
        TextView name;


        ContentHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_search_content);
        }

        @Override
        void bindHolder(Context context, final ContentItem item, final Callback callback) {
            super.bindHolder(context, item, callback);
            final Search search = item.search;
            final int placeHolder = R.drawable.holder;
            final int type = item.getType();
            switch (type) {
                case TYPE_GOODS:
                    Glide.with(context).load(search.goodsUrl)
                            .apply(new RequestOptions().fallback(placeHolder)
                                    .placeholder(placeHolder)
                                    .centerCrop()).into(icon);
                    name.setText(search.goodsName);
                    break;
                case TYPE_SHOP:
                    Glide.with(context).load(search.shopUrl)
                            .apply(new RequestOptions().fallback(placeHolder)
                                    .placeholder(placeHolder)
                                    .centerCrop()).into(icon);
                    name.setText(search.shopName);
                    break;
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        switch (type) {
                            case TYPE_SHOP:
                                callback.onShopItem(search.shopId, search.shopName);
                                break;
                            case TYPE_GOODS:
                                callback.onGoodsItem(search.goodsId, search.goodsName);
                                break;
                        }
                    }
                }
            });
        }
    }

    static class BorderHolder extends SearchHolder<BorderItem> {

        BorderHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_empty);
        }
    }

    static class StandardItem implements Item {
        private String search;

        StandardItem(String search) {
            this.search = search;
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

    static class BorderItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_BORDER;
        }
    }

    static class ContentItem implements Item {
        private Search search;
        @type
        private int type;

        ContentItem(Search searchData) {
            this.search = searchData;
        }

        public int getType() {
            if (getGoodsID() != null && getGoodsName() != null) {
                type = TYPE_GOODS;
            }
            if (getShopID() != null && getShopName() != null) {
                type = TYPE_SHOP;
            }
            return type;
        }

        public String getShopID() {
            return !TextUtils.isEmpty(search.shopId) ? search.shopId : null;
        }

        public String getShopName() {
            return !TextUtils.isEmpty(search.shopName) ? search.shopName : null;
        }

        public String getGoodsID() {
            return !TextUtils.isEmpty(search.goodsId) ? search.goodsId : null;
        }

        public String getGoodsName() {
            return !TextUtils.isEmpty(search.goodsName) ? search.goodsName : null;
        }

        @Override
        public int getViewType() {
            return VIEW_CONTENT;
        }
    }

    interface Item {
        int VIEW_STANDARD = 0;
        int VIEW_EMPTY = 1;
        int VIEW_CONTENT = 2;
        int VIEW_BORDER = 3;

        @IntDef({
                VIEW_STANDARD,
                VIEW_EMPTY,
                VIEW_CONTENT,
                VIEW_BORDER
        })
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    final static int TYPE_GOODS = 0;
    final static int TYPE_SHOP = 1;

    @IntDef({
            TYPE_GOODS,
            TYPE_SHOP
    })
    @interface type {
    }

    abstract static class SearchHolder<II extends Item> extends BaseHolder {
        SearchHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        void bindHolder(Context context, II item, Callback callback) {

        }
    }

    interface Callback {
        void onSearchItem(String search);

        void onShopItem(String shopID, String shopName);

        void onGoodsItem(String goodsID, String goodsName);
    }

}
