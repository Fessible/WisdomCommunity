package com.example.com.wisdomcommunity.ui.person.info.username;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_NAME;

/**
 * Created by rhm on 2018/2/28.
 */

public class UserNameFragment extends BaseFragment {
    public static final String TAG_USER_NAME_FRAGMENT = "USER_NAME_FRAGMENT";
    @BindView(R.id.user_name)
    EditText userName;

    @Override
    public int getResLayout() {
        return R.layout.fragment_username;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String name = bundle.getString(KEY_NAME);
        userName.setText(name);
    }

    @Override
    protected void destroyView() {

    }
}
