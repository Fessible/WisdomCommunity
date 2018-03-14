package com.example.com.wisdomcommunity.ui.shop.pay;

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
import static com.example.com.wisdomcommunity.ui.person.info.signature.SignatureFragment.MAX_LENGTH;

/**
 * Created by rhm on 2018/3/14.
 */

public class RemarkFragment extends BaseFragment {
    public static final String TAG_REMARK_FRAGMENT = "REMARK_FRAGMENT";
    public static final String KEY_REMARK = "remark";
    @BindView(R.id.edit_signature)
    EditText editText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sure)
    TextView sure;

    @BindView(R.id.txt_number)
    TextView txtNumber;

    @BindView(R.id.title)
    TextView title;

    private String remark;

    @Override
    public int getResLayout() {
        return R.layout.fragment_signature;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        title.setText(R.string.edit_remark);
        editText.setHint(R.string.edit_hint_remark);
        editText.addTextChangedListener(textWatcher);

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
            remark = s.toString();
        }
    };

    @OnClick(R.id.sure)
    public void sure() {
        if (remark.isEmpty()) {
            showShortToast(getString(R.string.please_edit_signature));
            return;
        }
        Intent data = new Intent();
        data.putExtra(KEY_REMARK, remark);
        getActivity().setResult(RESULT_OK, data);
        getActivity().finish();
    }

    @Override
    protected void destroyView() {

    }
}
