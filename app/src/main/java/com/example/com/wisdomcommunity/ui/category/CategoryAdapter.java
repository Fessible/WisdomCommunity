package com.example.com.wisdomcommunity.ui.category;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.home.Category;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 分类适配器
 * Created by rhm on 2018/4/12.
 */
public class CategoryAdapter extends BaseAdapter<CategoryAdapter.CategoryHolder> {
    private Context context;
    private Callback callback;
    private List<Category> categoryList = new ArrayList<>();

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Category> categories) {
        this.categoryList = categories;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void destroy() {
        if (categoryList != null) {
            categoryList.clear();
        }
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.bindHolder(context, categoryList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryHolder extends BaseHolder {

        @BindView(R.id.shop_icon)
        ImageView shopIcon;

        @BindView(R.id.shop_name)
        TextView shopName;

        @BindView(R.id.enter)
        TextView enter;

        CategoryHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_shop_header);
        }

        void bindHolder(Context context, final Category category, final Callback callback) {
            final int placeHolderId = R.drawable.app_icon;
            Glide.with(context).load(category.shopUrl)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(placeHolderId)
                            .fallback(placeHolderId))
                    .into(shopIcon);
            shopName.setText(category.shopName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(category);
                    }
                }
            });
        }
    }

    interface Callback {
        void onItemClick(Category category);
    }
}
