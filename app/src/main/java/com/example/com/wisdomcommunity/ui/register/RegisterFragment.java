package com.example.com.wisdomcommunity.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.RegisterContract;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.login.LoginFragment.PHONE_NUMBER_LENGHT;

/**
 * Created by rhm on 2018/3/21.
 */

public class RegisterFragment extends BaseFragment implements RegisterContract.View {
    public static final String TAG_REGISTER_FRAGMENT = "REGISTER_FRAGMENT";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PASSWORD = "password";

    @BindView(R.id.text_input_phone)
    EditText textInputPhone;

    @BindView(R.id.input_user_name)
    EditText inputUserName;

    @BindView(R.id.text_input_password)
    EditText inputPassword;

    @BindView(R.id.confirm)
    View confirm;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String phone;
    private String password;
    private RegisterContract.Presenter registerPresenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_register_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(navigationListener);
        inputPassword.addTextChangedListener(passwordWatcher);
        inputUserName.addTextChangedListener(userNameWatcher);
        textInputPhone.addTextChangedListener(phoneWatcher);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerPresenter = new RegisterPresenter(getContext(), RegisterFragment.this);
    }

    private View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            back();
        }
    };

    public void back() {
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            Intent data = new Intent();
            data.putExtra(KEY_PHONE, phone);
            data.putExtra(KEY_PASSWORD, password);
            getActivity().setResult(Activity.RESULT_OK, data);
        }
        getActivity().finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateConfirmStatus();
        }
    };

    private TextWatcher userNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateConfirmStatus();
        }
    };

    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateConfirmStatus();
        }
    };


    private void updateConfirmStatus() {
        if (inputUserName != null && textInputPhone != null && inputPassword != null && confirm != null) {
            confirm.setEnabled(!TextUtils.isEmpty(inputUserName.getText()) && !TextUtils.isEmpty(textInputPhone.getText())
                    && textInputPhone.getText().length() == PHONE_NUMBER_LENGHT
                    && !TextUtils.isEmpty(inputPassword.getText()) && !TextUtils.isEmpty(inputUserName.getText()));
        }
    }

    @OnClick(R.id.confirm)
    public void regist() {
        if (registerPresenter != null) {
            registerPresenter.register(inputUserName.getText().toString(), "", textInputPhone.getText().toString(), inputPassword.getText().toString());
        }

    }


    @Override
    protected void destroyView() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onUnauthorized() {

    }

    @Override
    public void registerSuccess(String phone, String password, String msg) {
        this.phone = phone;
        this.password = password;
        back();
    }

    @Override
    public void regitsterFailure(String msg) {
        showShortToast(msg);
    }
}
