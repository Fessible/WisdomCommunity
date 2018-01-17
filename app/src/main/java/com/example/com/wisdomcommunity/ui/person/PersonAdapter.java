package com.example.com.wisdomcommunity.ui.person;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rhm on 2018/1/17.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder> {

    private List<Item> itemList = new ArrayList<>();

    private Context mContext;

    public PersonAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.add(new Item(R.drawable.icon_location,"地址管理"));
            itemList.add(new Item(R.drawable.ic_feedback, "意见反馈"));
            itemList.add(new Item(R.drawable.ic_setting, "设置"));
            itemList.add(new Item(R.drawable.btn_online_customer_service, "客服"));
        }
    };

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PersonHolder holder = new PersonHolder(mContext, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(PersonHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class PersonHolder extends BaseHolder {
        @BindView(R.id.icon)
        ImageView image;

        @BindView(R.id.title)
        TextView title;

        PersonHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_person_item);
        }

        void bindHolder(Context context, Item item) {
            title.setText(item.getTitle());
            image.setImageResource(item.getImgRes());
        }
    }

    public static class Item {
        private int imgRes;
        private String title;

        public Item(@DrawableRes int imgRes, String title) {
            this.imgRes = imgRes;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public int getImgRes() {
            return imgRes;
        }
    }


}
