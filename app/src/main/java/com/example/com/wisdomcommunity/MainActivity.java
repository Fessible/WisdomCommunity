package com.example.com.wisdomcommunity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.example.com.wisdomcommunity.base.BaseActivity;
import com.example.com.wisdomcommunity.ui.home.HomeFragment;
import com.example.com.wisdomcommunity.ui.login.LoginFragment;
import com.example.com.wisdomcommunity.ui.order.OrderFragment;
import com.example.com.wisdomcommunity.ui.person.PersonFragment;
import com.example.com.wisdomcommunity.ui.shop.ShopFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private final static int HOME_INDEX = 0;
    private final static int SHOP_INDEX = 1;
    private final static int ORDER_INDEX = 2;
    private final static int PERSON_INDEX = 3;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private Fragment[] mFragment;
    private int mIndex;
    //test
    public int author = 0;
    public final static int REQUEST_CODE = 1;

    @Override
    public int getResLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        if (author == 0) {
            IntentUtil.startTemplateActivityForResult(MainActivity.this, LoginFragment.class, null, LoginFragment.TAG_Login_FRAGMENT, REQUEST_CODE);

        }
        radioGroup.setOnCheckedChangeListener(this);
        initFragment();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
            }

        }
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //初始化数组
        HomeFragment mHomeFragment = new HomeFragment();
        ShopFragment mShopFragment = new ShopFragment();
        OrderFragment mOrderFragment = new OrderFragment();
        PersonFragment mPersonFragmet = new PersonFragment();
        mFragment = new Fragment[]{mHomeFragment, mShopFragment, mOrderFragment, mPersonFragmet};
        fragmentTransaction.replace(R.id.content_layout, mHomeFragment).commit();
        mIndex = HOME_INDEX;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                setIndexSelected(HOME_INDEX);
                break;
            case R.id.rb_shop:
                setIndexSelected(SHOP_INDEX);
                break;
            case R.id.rb_order:
                setIndexSelected(ORDER_INDEX);
                break;
            case R.id.rb_person:
                setIndexSelected(PERSON_INDEX);
                break;
        }
    }

    private void setIndexSelected(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //如果点击仍然是当前位置直接返回
        if (mIndex == index) {
            return;
        }
        //隐藏上一个位置内容
        fragmentTransaction.hide(mFragment[mIndex]);
        //如果未添加到事务中
        if (!mFragment[index].isAdded()) {
            fragmentTransaction.add(R.id.content_layout, mFragment[index]);
        } else {//已经添加到事务中直接显示
            fragmentTransaction.show(mFragment[index]);
        }
        fragmentTransaction.commit();
        mIndex = index;
    }
}
