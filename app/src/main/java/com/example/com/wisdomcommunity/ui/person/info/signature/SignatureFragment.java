package com.example.com.wisdomcommunity.ui.person.info.signature;

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
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_SIGNATURE;

/**
 * Created by rhm on 2018/2/28.
 */

public class SignatureFragment extends BaseFragment {
    public static final String TAG_SIGNATURE_FRAGMENT = "SIGNATURE_FRAGMENT";
    public static final String KEY_SIGNATURE = "signature";
    public static final int MAX_LENGTH = 50;
    @BindView(R.id.edit_signature)
    EditText editText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sure)
    TextView sure;

    @BindView(R.id.txt_number)
    TextView txtNumber;

    private String signature;

    @Override
    public int getResLayout() {
        return R.layout.fragment_signature;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String signature = bundle.getString(KEY_SIGNATURE);
        editText.setText(signature);
        editText.setSelection(signature != null ? signature.length() : 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        editText.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int length;
            if (s == null) {
                length = 0;
            } else {
                length = s.length();
            }
            if (length > 0) {
                sure.setEnabled(true);
            }
            txtNumber.setText(getContext().getString(R.string.edit_max, length, MAX_LENGTH));
        }

        @Override
        public void afterTextChanged(Editable s) {
            signature = s.toString();
        }
    };

    @OnClick(R.id.sure)
    public void sure() {
        if (signature.isEmpty()) {
            showShortToast(getString(R.string.please_edit_signature));
            return;
        }
        Intent data = new Intent();
        data.putExtra(KEY_SIGNATURE, signature);
        getActivity().setResult(RESULT_OK, data);
        getActivity().finish();
    }

    @Override
    protected void destroyView() {

    }
}
