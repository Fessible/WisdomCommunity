package com.example.com.wisdomcommunity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.wisdomcommunity.base.BaseActivity;
import com.example.com.wisdomcommunity.localsave.ShopCart;
import com.example.com.wisdomcommunity.receiver.CartReceiver;
import com.example.com.wisdomcommunity.receiver.UserReceiver;
import com.example.com.wisdomcommunity.ui.home.HomeFragment;
import com.example.com.wisdomcommunity.ui.order.OrderFragment;
import com.example.com.wisdomcommunity.ui.person.PersonFragment;
import com.example.com.wisdomcommunity.ui.shop.ShopFragment;
import com.example.com.wisdomcommunity.ui.shop.cart.CartFragment;
import com.example.com.wisdomcommunity.ui.splash.SplashFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private final static int HOME_INDEX = 0;
    private final static int SHOP_INDEX = 1;
    private final static int ORDER_INDEX = 2;
    private final static int PERSON_INDEX = 3;
    private final static int mTouchSclop = 10;
    public final static String ACTION_USER_CHANGED = "action_user_changed";
    public final static String ACTION_SHOP_CART_CHANGED = "action_shop_cart_changed";
    public final static String ACTION_SHOP_CART_CLEAR = "action_shop_cart_clear";


    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.icon_cart)
    ImageView iconCart;

    @BindView(R.id.relative_content)
    RelativeLayout relativeContent;

    @BindView(R.id.icon_cart_layout)
    RelativeLayout iconCartLayout;

    @BindView(R.id.rb_home)
    RadioButton rbHome;

    @BindView(R.id.shopping_cart_total_num)
    TextView cartTotalNum;

    private Fragment[] mFragment;
    private int mIndex;
    private int mDistance;
    private boolean isShowFloatImage = true;
    private float mDownY;
    private Timer timer;
    /**
     * 用户手指按下后抬起的实际
     */
    private long upTime;

    public final static int REQUEST_CODE = 1;
    private SplashFragment splashFragment;
    private CartReceiver cartReceiver;
    private UserReceiver userReceiver;


    @Override
    public int getResLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

        addSplashFragment();

        radioGroup.setOnCheckedChangeListener(this);
        initFragment();


        iconCartLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDistance = getDisplayMetrics() - iconCartLayout.getRight() + iconCartLayout.getWidth() / 2;
                iconCartLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeFragmentsIfExits(splashFragment);
            }
        }, 2000);


        cartReceiver = new CartReceiver() {
            @Override
            protected void onShopCartClear() {
                ShopCart.clear(MainActivity.this);
                cartTotalNum.setVisibility(View.GONE);
                iconCartLayout.setEnabled(false);
            }

            @Override
            protected void onShopCartChanged() {
                int count = ShopCart.getCount(MainActivity.this);
                if (count > 0) {
                    cartTotalNum.setVisibility(View.VISIBLE);
                    cartTotalNum.setText(String.valueOf(count));
                    iconCartLayout.setEnabled(true);
                } else {
                    cartTotalNum.setVisibility(View.GONE);
                    iconCartLayout.setEnabled(false);
                }

            }
        };
        userReceiver = new UserReceiver() {
            @Override
            protected void onUserChanged() {
                setIndexSelected(HOME_INDEX);
                radioGroup.clearCheck();
                rbHome.setChecked(true);
            }
        };
        IntentFilter userFilter = new IntentFilter();
        userFilter.addAction(ACTION_USER_CHANGED);

        IntentFilter cartFilter = new IntentFilter();
        cartFilter.addAction(ACTION_SHOP_CART_CHANGED);
        cartFilter.addAction(ACTION_SHOP_CART_CLEAR);

        registerReceiver(cartReceiver, cartFilter);
        registerReceiver(userReceiver, userFilter);
    }

    public void addSplashFragment() {
        splashFragment = new SplashFragment();
        addFragment(R.id.relative_content, splashFragment, SplashFragment.TAG_SPLASH_FRAGMENT);
    }


    private int getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
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

    //timer重新计时
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (System.currentTimeMillis() - upTime < 500) {
                    //本次按下距离上次的抬起小于0.5s时，取消Timer
                    timer.cancel();
                }
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownY - ev.getY()) > mTouchSclop) {
                    if (isShowFloatImage && mIndex == HOME_INDEX) {
                        hideFloatImage(mDistance);
                    }
                }
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!isShowFloatImage && mIndex == HOME_INDEX) {
                    upTime = System.currentTimeMillis();
                    timer = new Timer();
                    timer.schedule(new FloatTask(), 500);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showFloatImage(int distance) {

        isShowFloatImage = true;
        //移动
        TranslateAnimation ta = new TranslateAnimation(
                distance,//起始x坐标
                0,//结束x坐标
                0,//起始y坐标
                0);//结束y坐标（正数向下移动）
        ta.setDuration(300);

        //渐变动画
        AlphaAnimation al = new AlphaAnimation(0.5f, 1f);
        al.setDuration(300);

        AnimationSet set = new AnimationSet(true);//同时执行多个动画
        //动画完成后不回到原位
        set.setFillAfter(true);

        set.addAnimation(ta);
        set.addAnimation(al);
//        iconCart.startAnimation(set);
        iconCartLayout.startAnimation(set);
    }

    //移动到右边并变透明
    private void hideFloatImage(int distance) {
        isShowFloatImage = false;
        //位移动画
        TranslateAnimation ta = new TranslateAnimation(
                0,//起始x坐标,10表示与初始位置相距10
                distance,//结束x坐标
                0,//起始y坐标
                0);//结束y坐标（正数向下移动）
        ta.setDuration(300);

        //渐变动画
        AlphaAnimation al = new AlphaAnimation(1f, 0.5f);
        al.setDuration(300);

        AnimationSet set = new AnimationSet(true);
        //动画完成后不回到原位
        set.setFillAfter(true);
        set.addAnimation(ta);
        set.addAnimation(al);
//        iconCart.startAnimation(set);
        iconCartLayout.startAnimation(set);
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
//                iconCart.setVisibility(View.VISIBLE);
                iconCartLayout.setVisibility(View.VISIBLE);
                setIndexSelected(HOME_INDEX);
                break;
            case R.id.rb_shop:
                removeFloatingImage();
                setIndexSelected(SHOP_INDEX);
                break;
            case R.id.rb_order:
                removeFloatingImage();
                setIndexSelected(ORDER_INDEX);
                break;
            case R.id.rb_person:
                removeFloatingImage();
                setIndexSelected(PERSON_INDEX);
                break;
        }
    }

    private void removeFloatingImage() {
//        iconCart.clearAnimation();
//        iconCart.setVisibility(View.GONE);
        iconCartLayout.clearAnimation();
        iconCartLayout.setVisibility(View.GONE);
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

    private class FloatTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showFloatImage(mDistance);
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cartReceiver);
        unregisterReceiver(userReceiver);
    }

    @OnClick(R.id.icon_cart_layout)
    public void onCart() {
        IntentUtil.startTemplateActivity(MainActivity.this, CartFragment.class, CartFragment.TAG_CART_FRAGMENT);
    }
}
