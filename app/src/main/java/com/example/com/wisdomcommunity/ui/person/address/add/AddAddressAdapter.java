package com.example.com.wisdomcommunity.ui.person.address.add;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.com.support_business.Constants;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.support_business.Constants.GENDER_FEMALE;
import static com.example.com.support_business.Constants.GENDER_MALE;
import static com.example.com.support_business.Constants.TYPE_NAME;
import static com.example.com.support_business.Constants.TYPE_OTHER;
import static com.example.com.support_business.Constants.TYPE_PHONE;
import static com.example.com.wisdomcommunity.ui.person.address.add.AddAddressAdapter.Item.VIEW_CHOSE;
import static com.example.com.wisdomcommunity.ui.person.address.add.AddAddressAdapter.Item.VIEW_SEX;
import static com.example.com.wisdomcommunity.ui.person.address.add.AddAddressAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddAddressAdapter extends BaseAdapter<AddAddressAdapter.AddHolder> {

    private final static List<Item> itemList = new ArrayList<>();
    private Context mContext;
    private Address address;
    private String name;
    private String phone;
    private int sex;
    private String district;
    private String detailAddress;

    public AddAddressAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(adapter);
    }


    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick() {

        }

        @Override
        public void onSexItem(int iSex) {
            sex = iSex;
        }

        @Override
        public void onPhoneItem(String strPhone) {
            phone = strPhone;
        }

        @Override
        public void onNameItem(String strName) {
            name = strName;
        }

        @Override
        public void onDistrictItem(String strDistrict) {
            district = strDistrict;
        }

        @Override
        public void onAddressItem(String address) {
            detailAddress = address;
        }
    };

    private RecyclerView.AdapterDataObserver adapter = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (address != null) {
                name = address.name;
                phone = address.phone;
                sex = address.sex;
                district = address.district;
                detailAddress = address.address;
            }
            itemList.add(new StandardItem(mContext.getString(R.string.name), mContext.getString(R.string.name_hint), name, TYPE_NAME));
            itemList.add(new SexItem(sex));
            itemList.add(new StandardItem(mContext.getString(R.string.phone), mContext.getString(R.string.phont_hint), phone, Constants.TYPE_PHONE));
//            itemList.add(new ChoseItem(district));
            itemList.add(new StandardItem(mContext.getString(R.string.detail_address), mContext.getString(R.string.address_hint), detailAddress, Constants.TYPE_OTHER));
        }
    };

    public void setData(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    String getPhone() {
        return phone;
    }

    int getSex() {
        return sex;
    }

    String getDistrict() {
        return district;
    }

    String getDetailAddress() {
        return detailAddress;
    }

    @Override
    protected void destroy() {
        itemList.clear();
        unregisterAdapterDataObserver(adapter);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public AddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddHolder holder = null;
        switch (viewType) {
            case VIEW_STANDARD:
                holder = new StandardHolder(mContext, parent);
                break;
            case VIEW_CHOSE:
                holder = new ChoseHolder(mContext, parent);
                break;
            case VIEW_SEX:
                holder = new SexHolder(mContext, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AddHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class StandardHolder extends AddHolder<StandardItem> {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.edit)
        EditText edit;

        private int type;
        private OnItemClickListener clickListener;

        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_standard);
        }

        @Override
        public void bindHolder(Context context, StandardItem Item, OnItemClickListener clickListener) {
            if (!TextUtils.isEmpty(Item.value)) {
                edit.setText(Item.value);
                edit.setSelection(Item.value.length());
            }

            type = Item.type;
            this.clickListener = clickListener;
            title.setText(Item.title);
            edit.setHint(Item.hint);
            edit.addTextChangedListener(textWatcher);
            switch (type) {
                case TYPE_NAME:
                    edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    break;
                case TYPE_PHONE:
                    edit.setInputType(InputType.TYPE_CLASS_PHONE);
                    edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    break;
                case TYPE_OTHER:
                    break;
            }
        }

        private TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                if (clickListener != null) {
                    switch (type) {
                        case TYPE_PHONE:
                            clickListener.onPhoneItem(value);
                            break;
                        case TYPE_NAME:
                            clickListener.onNameItem(value);
                            break;
                        case TYPE_OTHER://详细地址
                            clickListener.onAddressItem(value);
                            break;
                    }
                }
            }
        };
    }

    static class ChoseHolder extends AddHolder<ChoseItem> {
        ChoseHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_chose_area);
        }

        @Override
        public void bindHolder(Context context, ChoseItem Item, OnItemClickListener clickListener) {
            if (clickListener != null) {
                clickListener.onItemClick();
            }
        }
    }

    static class SexHolder extends AddHolder<SexItem> {
        @BindView(R.id.rb_men)
        RadioButton rbMen;

        @BindView(R.id.rb_woman)
        RadioButton rbWoman;

        @BindView(R.id.radioGroup)
        RadioGroup radioGroup;

        SexHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_chose);
        }

        @Override
        public void bindHolder(final Context context, SexItem Item, final OnItemClickListener clickListener) {

            rbMen.setChecked(Item.sex == GENDER_MALE);
            rbWoman.setChecked(Item.sex == GENDER_FEMALE);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                    switch (checkId) {
                        case R.id.rb_men:
                            if (clickListener != null) {
                                clickListener.onSexItem(GENDER_MALE);
                            }
                            break;
                        case R.id.rb_woman:
                            clickListener.onSexItem(GENDER_FEMALE);
                            break;
                    }
                }
            });
        }
    }


    class StandardItem implements Item {
        private String title;
        private String hint;
        private int type;
        private String value;

        public StandardItem(String title, String hint, String value, @Constants.Type int type) {
            this.title = title;
            this.hint = hint;
            this.type = type;
            this.value = value;
        }

        public int getType() {
            return type;
        }

        @Override
        public int getViewType() {
            return VIEW_STANDARD;
        }
    }

    class ChoseItem implements Item {
        private String district;

        public ChoseItem(String district) {
            this.district = district;
        }

        @Override
        public int getViewType() {
            return VIEW_CHOSE;
        }
    }

    class SexItem implements Item {
        private int sex;

        SexItem(int sex) {
            this.sex = sex;
        }

        @Override
        public int getViewType() {
            return VIEW_SEX;
        }
    }


    interface Item {

        int VIEW_STANDARD = 0;
        int VIEW_CHOSE = 1;
        int VIEW_SEX = 2;

        @IntDef({VIEW_STANDARD, VIEW_CHOSE, VIEW_SEX})
        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    static abstract class AddHolder<II extends Item> extends BaseHolder {

        public AddHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public abstract void bindHolder(Context context, II Item, OnItemClickListener listener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick();

        void onSexItem(int sex);

        void onPhoneItem(String phone);

        void onNameItem(String name);

        void onDistrictItem(String district);

        void onAddressItem(String address);
    }

}
