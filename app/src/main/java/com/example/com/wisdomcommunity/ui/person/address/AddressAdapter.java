package com.example.com.wisdomcommunity.ui.person.address;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.address.AddressAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.person.address.AddressAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/2/27.
 */

public class AddressAdapter extends BaseAdapter<AddressAdapter.AddressHolder> {

    private Context mContext;
    private List<Address> addressList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private final List<Item> itemList = new ArrayList<>();

    AddressAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            if (addressList != null && !addressList.isEmpty()) {
                for (int i = 0; i < addressList.size(); i++) {
                    itemList.add(new StandardItem(addressList.get(i)));
                }
            } else {
                itemList.add(new EmptyItem());
            }
        }
    };

    void setItemData(Address address, int position) {
        StandardItem standardItem = (StandardItem) itemList.get(position);
        standardItem.address = address;
        notifyItemChanged(position);
    }

    void removeItem(int position) {
        addressList.remove(position);
        notifyDataSetChanged();
    }

    public void setData(List<Address> addressList) {
        this.addressList = addressList;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    protected void destroy() {
        addressList.clear();
        unregisterAdapterDataObserver(observer);
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressHolder holder = null;
        switch (viewType) {
            case VIEW_STANDARD:
                holder = new StandardHolder(mContext, parent);
                break;
            case VIEW_EMPTY:
                holder = new EmptyHolde(mContext, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position), position, onItemClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class StandardHolder extends AddressHolder<StandardItem> {
        @BindView(R.id.name)
        TextView tvName;

        @BindView(R.id.address)
        TextView tvAddress;

        @BindView(R.id.phone)
        TextView tvPhone;
        
        @BindView(R.id.edit_layout)
        RelativeLayout editlayout;

        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_manager);
        }

        @Override
        public void bindHolder(Context context, StandardItem item, final int position, final OnItemClickListener onItemClickListener) {
            final Address address = item.address;
            tvName.setText(address.name);
            tvAddress.setText(address.address);
            tvPhone.setText(address.phone);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(address, position);
                    }
                }
            });
            editlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.editItem(address, position);
                    }
                }
            });
        }
    }

    static class EmptyHolde extends AddressHolder<EmptyItem> {
        public EmptyHolde(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_empty);
        }

        @Override
        public void bindHolder(Context context, EmptyItem item, int position, OnItemClickListener onItemClickListener) {

        }
    }


    static class StandardItem implements Item {
        private Address address;

        public StandardItem(Address address) {
            this.address = address;
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

    interface Item {
        final static int VIEW_STANDARD = 0;
        final static int VIEW_EMPTY = 1;

        @IntDef({VIEW_STANDARD, VIEW_EMPTY})

        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }


    abstract static class AddressHolder<II extends Item> extends BaseHolder {
        public AddressHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public abstract void bindHolder(Context context, II item, int position, final OnItemClickListener onItemClickListener);

    }

    public interface OnItemClickListener {
        void onItemClick(Address addressId, int position);

        void editItem(Address address, int position);
    }
}
