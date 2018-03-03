package com.example.com.wisdomcommunity.ui.person.feedback;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.FeedbackContract;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rhm on 2018/2/28.
 */

public class FeedbackFragment extends BaseFragment implements FeedbackContract.View {
    public static final String TAG_FEEDBACK_FRAGMENT = "FEEDBACK_FRAGMENT";
    @BindView(R.id.edit_feedback)
    EditText editFeedback;

    @BindView(R.id.txt_number)
    TextView txtNumber;

    @BindView(R.id.submit)
    TextView submit;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FeedBbackPresenter presenter;
    private String suggestion;
    private int length;
    public static final int MAX_LENGTH = 100;


    @Override
    public int getResLayout() {
        return R.layout.fragment_feedback_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        editFeedback.addTextChangedListener(textWatcher);
        editFeedback.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
        presenter = new FeedBbackPresenter(getContext(), FeedbackFragment.this);
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
            length = 0;
            if (s == null) {
                length = 0;
            } else {
                length = s.length();
            }
            if (length > 0) {
                submit.setEnabled(true);
            }
            txtNumber.setText(getContext().getString(R.string.edit_max, length, MAX_LENGTH));
        }

        @Override
        public void afterTextChanged(Editable s) {
            suggestion = s.toString();
        }
    };


    @OnClick(R.id.submit)
    public void submit() {
        if (TextUtils.isEmpty(suggestion)) {
            showShortToast(getString(R.string.please_edit_feedback));
            return;
        }
        if (presenter != null) {
            presenter.submitSuggestion(suggestion);
        }
    }

    @Override
    protected void destroyView() {
    }

    @Override
    public void submitSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showShortToast(msg);
        }
        getActivity().finish();
    }

    @Override
    public void submitFailure(String msg) {
        showShortToast(msg);
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
