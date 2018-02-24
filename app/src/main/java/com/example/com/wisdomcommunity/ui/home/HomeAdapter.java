package com.example.com.wisdomcommunity.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主界面适配器
 * Created by rhm on 2018/1/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Homeholder> {

    private Context mContext;
    private List<Recommend> recommendList = new ArrayList<>();

    public HomeAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Recommend> recommendList) {
        this.recommendList = recommendList;
    }

    @Override
    public Homeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Homeholder homeholder = new Homeholder(mContext, parent);
        return homeholder;
    }

    @Override
    public void onBindViewHolder(Homeholder holder, int position) {
        holder.bindHolder(mContext, recommendList.get(position));
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }

    static class Homeholder extends BaseHolder {
        @BindView(R.id.imge)
        ImageView imge;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.name)
        TextView name;

        Homeholder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_home_item);
        }

        void bindHolder(Context context, Recommend recommend) {
            name.setText(recommend.name);
            price.setText(recommend.price);
            Glide.with(context)
                    .load(recommend.goodsUrl)
                    .into(imge);
        }
    }
}





