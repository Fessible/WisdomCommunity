package com.example.com.wisdomcommunity.ui.person;

import android.content.Context;
import android.support.annotation.DrawableRes;
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
 * Created by rhm on 2018/1/17.
 */

public class PersonAdapter extends BaseAdapter<PersonAdapter.PersonHolder> {
    public final static int TYPE_ADDRESS = 0;
    public final static int TYPE_FEEDBACK = 1;
    public final static int TYPE_SET = 2;
    public final static int TYPE_SERVICE = 3;
    private List<Item> itemList = new ArrayList<>();

    private Context mContext;
    private onItemClickListener onItemClickListener;

    public PersonAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(observer);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.add(new Item(R.drawable.icon_location, mContext.getString(R.string.address_manager), TYPE_ADDRESS));
            itemList.add(new Item(R.drawable.ic_feedback, mContext.getString(R.string.feedback), TYPE_FEEDBACK));
            itemList.add(new Item(R.drawable.ic_setting, mContext.getString(R.string.settiing), TYPE_SET));
            itemList.add(new Item(R.drawable.btn_online_customer_service, mContext.getString(R.string.service), TYPE_SERVICE));
        }
    };

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PersonHolder holder = new PersonHolder(mContext, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(PersonHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    protected void destroy() {
        itemList.clear();
        unregisterAdapterDataObserver(observer);
    }

    static class PersonHolder extends BaseHolder {
        @BindView(R.id.icon)
        ImageView image;

        @BindView(R.id.title)
        TextView title;

        PersonHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_person_item);
        }

        void bindHolder(Context context, final Item item, final onItemClickListener clickListener) {
            title.setText(item.getTitle());
            image.setImageResource(item.getImgRes());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(item.type);
                    }
                }
            });
        }
    }

    public static class Item {
        private int imgRes;
        private String title;
        private int type;

        public Item(@DrawableRes int imgRes, String title, int type) {
            this.imgRes = imgRes;
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public int getImgRes() {
            return imgRes;
        }
    }

    public interface onItemClickListener {
        void onItemClick(int type);
    }

}
