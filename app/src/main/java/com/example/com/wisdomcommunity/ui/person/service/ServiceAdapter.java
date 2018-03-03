package com.example.com.wisdomcommunity.ui.person.service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
            serviceItemList.add(new ServiceItem("如何查看订单", content[0]));
            serviceItemList.add(new ServiceItem("订单信息填写错误", content[1]));
            serviceItemList.add(new ServiceItem("如何取消订单", content[2]));
            serviceItemList.add(new ServiceItem("商品如何退换货", content[3]));
        }
    };

    //test data
    private String[] content = {"在我的订单中可以查看购买的商品订单及物流状态", "您可以联系客服反馈您的情况，更正为正确的地址或电话信息", "如果您的订单未发货可以取消订单", "在该商品订单中选择售后服务，上传退换商品的图片及文字描述，提交成功后，客服为您受理退货"};

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
        private String content;

        public ServiceItem(String title, String content) {
            this.content = content;
            this.title = title;
        }

    }

    static class ServiceHolder extends BaseHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.arrow)
        ImageView arrow;

        public ServiceHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_service);
        }

        public void bindHolder(final Context context, final ServiceItem item, final OnItemClickListener clickListener) {
            title.setText(item.title);
            content.setText(item.content);

            content.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick();
                    }
                    if (content.getVisibility() == View.VISIBLE) {
                        content.setVisibility(View.GONE);
                        arrow.setBackgroundResource(R.drawable.right_arrow);
                    } else {
                        content.setVisibility(View.VISIBLE);
                        arrow.setBackgroundResource(R.drawable.arrow_down);
                    }

                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick();
    }

}
