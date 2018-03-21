package com.example.com.wisdomcommunity.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CheckableImageButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.LoginContract;
import com.example.com.wisdomcommunity.ui.forget.ForgetFragment;
import com.example.com.wisdomcommunity.ui.register.RegisterFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static android.accounts.AccountManager.KEY_PASSWORD;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rhm on 2018/1/16.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View{

    public static final String TAG_Login_FRAGMENT = "LOGIN_FRAGMENT";
    public static final int REQUEST_CODE = 1;
    public static final int PHONE_NUMBER_LENGHT = 11;


    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.text_input_phone)
    EditText editPhone;

    @BindView(R.id.text_clear)
    ImageButton textClear;

    @BindView(R.id.text_input_password)
    EditText editPassword;

    @BindView(R.id.password_toggle)
    CheckableImageButton passwordToggle;

    @BindView(R.id.login)
    TextView login;

    @BindView(R.id.forgot_password)
    TextView forgotPassword;

    @Override
    public int getResLayout() {
        return R.layout.fragment_login_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        textClear.setVisibility(View.INVISIBLE);
        editPhone.addTextChangedListener(phoneWatcher);
        editPassword.addTextChangedListener(passWatcher);
        //添加软件盘监听,输入完成后点击键盘完成
        editPassword.setOnEditorActionListener(editorActionListener);
//        setImmersiveStatusBar(true, Color.WHITE);
    }

    @Override
    protected void destroyView() {

    }

    @OnClick(R.id.text_clear)
    public void onClear() {
        if (editPhone != null) {
            editPhone.setText("");
            editPhone.requestFocus();
        }
    }

    @OnClick(R.id.login)
    public void login() {
        if (editPhone != null && editPassword != null) {
            String phone = editPhone.getText().toString();
            String password = editPassword.getText().toString();
            //test
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
            showShortToast("登录成功");

//            HttpRequest.loginRequest(phone, password, new DisposeListener() {
//                @Override
//                public void onSuccess(Object responseObj) {
//                    LoginModel loginModel = (LoginModel) responseObj;
//                    showShortToast(loginModel.emsg);
//
//                    getActivity().setResult(RESULT_OK);
//                    getActivity().finish();
//                }
//
//                @Override
//                public void onFailure(String msg) {
//                    showShortToast(msg);
//                }
//            });
        }

    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.password_toggle)
    public void passwordToggle() {
        if (editPassword != null && passwordToggle != null) {
            int selection = editPassword.getSelectionEnd();
            //当前状态为密码类型，转换为无类型
            if (editPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                editPassword.setTransformationMethod(null);
                passwordToggle.setChecked(true);
            } else {
                editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置为密码类型
                passwordToggle.setChecked(false);
            }
            editPassword.setSelection(selection);
        }

    }

    @OnClick(R.id.forgot_password)
    public void forgetPassword() {
        IntentUtil.startTemplateActivityForResult(LoginFragment.this, ForgetFragment.class, ForgetFragment.TAG_FORGET_FRAGMENT, REQUEST_CODE);
    }

    @OnClick(R.id.sign_up)
    public void register() {
        IntentUtil.startTemplateActivityForResult(LoginFragment.this, RegisterFragment.class, RegisterFragment.TAG_REGISTER_FRAGMENT, REQUEST_CODE);

    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (v.getId()) {
                case R.id.text_input_password:
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (login != null && login.isEnabled()) {
                            login();
                        }
                    }
            }
            return false;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
//                    String phone = data.getStringExtra(KEY_PHONE);
                    String password = data.getStringExtra(KEY_PASSWORD);
//                    editPhone.setText(phone);
                    editPassword.setText(password);
                    login();
                    break;
            }
        }
    }

    private TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (textClear != null) {
                textClear.setVisibility(!TextUtils.isEmpty(s) ? View.VISIBLE : View.INVISIBLE);
            }
            updateLoginStatus();//当密码已输入，登录按钮可点击
        }
    };

    private void updateLoginStatus() {
        if (editPhone != null && editPassword != null && login != null) {
            login.setEnabled(!TextUtils.isEmpty(editPassword.getText())
                    && editPhone.getText().length() == PHONE_NUMBER_LENGHT
                    && !TextUtils.isEmpty(editPhone.getText()));
        }
    }

    private TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateLoginStatus();
        }
    };

    @Override
    public void onLoginSuccess(CharSequence phone, CharSequence password, String userId) {

    }

    @Override
    public void onLoginFailure(String msg) {

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
}
