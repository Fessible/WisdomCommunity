package com.example.com.wisdomcommunity.ui.person.service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rhm on 2018/2/28.
 */

public class ServiceAdapter extends BaseAdapter<ServiceAdapter.ServiceHolder> {
    private final List<ServiceItem> serviceItemList = new ArrayList<>();
    private OnItemClickListener clickListener;
    private Context context;

    public ServiceAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            serviceItemList.add(new ServiceItem("如何查看订单"));
            serviceItemList.add(new ServiceItem("订单信息填写错误"));
            serviceItemList.add(new ServiceItem("如何取消订单"));
            serviceItemList.add(new ServiceItem("商品如何退换货"));
        }
    };

    @Override
    protected void destroy() {
        serviceItemList.clear();
        unregisterAdapterDataObserver(observer);
    }

    public void setClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServiceHolder holder = new ServiceHolder(context, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
        holder.bindHolder(context, serviceItemList.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return serviceItemList.size();
    }

    static class ServiceItem {
        private String title;

        public ServiceItem(String title) {
            this.title = title;
        }

    }

    static class ServiceHolder extends BaseHolder {
        @BindView(R.id.title)
        TextView title;

        public ServiceHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_service);
        }

        public void bindHolder(Context context, ServiceItem item, final OnItemClickListener clickListener) {
            title.setText(item.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick();
                    }
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick();
    }

}
