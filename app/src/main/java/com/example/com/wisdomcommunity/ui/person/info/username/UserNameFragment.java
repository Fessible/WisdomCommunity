package com.example.com.wisdomcommunity.ui.person.info.username;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_NAME;

/**
 * Created by rhm on 2018/2/28.
 */

public class UserNameFragment extends BaseFragment {
    public static final String TAG_USER_NAME_FRAGMENT = "USER_NAME_FRAGMENT";
    public static final String KEY_USER_NAME = "user_name";
    @BindView(R.id.user_name)
    EditText userName;

    @BindView(R.id.sure)
    TextView sure;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String strUserName;

    @Override
    public int getResLayout() {
        return R.layout.fragment_username;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String name = bundle.getString(KEY_NAME);
        userName.setText(name);
        userName.setSelection(name != null ? name.length() : 0);
        userName.addTextChangedListener(textWatcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int length = s.length();
            if (length >= 4) {
                sure.setEnabled(true);
            } else {
                sure.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            strUserName = s.toString();
        }
    };

    @OnClick(R.id.sure)
    public void sure() {
        Intent intent = new Intent();
        intent.putExtra(KEY_USER_NAME, strUserName);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected void destroyView() {

    }
}
