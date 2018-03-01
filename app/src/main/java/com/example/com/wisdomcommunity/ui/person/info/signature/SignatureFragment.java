package com.example.com.wisdomcommunity.ui.person.info.signature;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_SIGNATURE;

/**
 * Created by rhm on 2018/2/28.
 */

public class SignatureFragment extends BaseFragment {
    public static final String TAG_SIGNATURE_FRAGMENT = "SIGNATURE_FRAGMENT";
    @BindView(R.id.edit_signature)
    EditText editText;

    @Override
    public int getResLayout() {
        return R.layout.fragment_signature;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String signature = bundle.getString(KEY_SIGNATURE);
        editText.setText(signature);
    }

    @Override
    protected void destroyView() {

    }
}
