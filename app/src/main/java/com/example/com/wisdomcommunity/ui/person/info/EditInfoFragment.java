package com.example.com.wisdomcommunity.ui.person.info;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.personal.Info;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.EditInfoContract;
import com.example.com.wisdomcommunity.ui.person.info.signature.SignatureFragment;
import com.example.com.wisdomcommunity.ui.person.info.username.UserNameFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_DISTRICT;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_HEAD_IMAGE;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_NAME;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_SEX;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_SIGNATURE;

/**
 * Created by rhm on 2018/2/28.
 */

public class EditInfoFragment extends BaseFragment implements EditInfoContract.View {
    public static final String TAG_INFO_FRAGMENT = "INFO_FRAGMENT";
    public static final int REQUEST_NAME = 0;
    public static final int REQUEST_SIGNATURE = 1;
    public static final String KEY_NAME = "user_name";
    public static final String KEY_SIGNATURE = "signature";
    private BottomSheetDialog photoSheetDialog;
    private BottomSheetDialog sexBottomDialog;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private EditInfoAdapter adapter;
    private EditInfoPresenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_edit_person;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter = new EditInfoPresenter(getContext(), EditInfoFragment.this);
        adapter = new EditInfoAdapter(getContext());
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
                        .build()).build());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setClickListener(new EditInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int type) {
                switch (type) {
                    case TYPE_HEAD_IMAGE:
                        if (photoSheetDialog == null) {
                            photoSheetDialog = new BottomSheetDialog(getContext(), R.style.Theme_Design_Admin_BottomSheetDialog);
                            photoSheetDialog.setContentView(R.layout.dialog_edit_head_image);
                            photoSheetDialog.findViewById(R.id.take_a_picture).setOnClickListener(editAcatarOnClickListener);//拍照
                            photoSheetDialog.findViewById(R.id.select_from_album).setOnClickListener(editAcatarOnClickListener);//相册中选择
                            photoSheetDialog.findViewById(R.id.opt_cancel).setOnClickListener(editAcatarOnClickListener);
                        }
                        photoSheetDialog.show();

                        break;
                    case TYPE_DISTRICT:
                        break;
                    case TYPE_NAME:
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_NAME, "张三");
                        IntentUtil.startSecondActivityForResult(EditInfoFragment.this, UserNameFragment.class, bundle, UserNameFragment.TAG_USER_NAME_FRAGMENT, REQUEST_NAME);
                        break;
                    case TYPE_SEX:
                        if (sexBottomDialog == null) {
                            sexBottomDialog = new BottomSheetDialog(getContext(), R.style.Theme_Design_Admin_BottomSheetDialog);
                            sexBottomDialog.setContentView(R.layout.dialog_edit_head_image);
                            TextView men = sexBottomDialog.findViewById(R.id.take_a_picture);
                            men.setText(getContext().getString(R.string.men));
                            men.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                            TextView women = sexBottomDialog.findViewById(R.id.select_from_album);
                            women.setText(getContext().getString(R.string.woman));
                            women.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            sexBottomDialog.findViewById(R.id.opt_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sexBottomDialog.dismiss();
                                }
                            });
                        }
                        sexBottomDialog.show();
                        break;
                    case TYPE_SIGNATURE:
                        Bundle signature = new Bundle();
                        signature.putString(KEY_SIGNATURE, "张三");
                        IntentUtil.startSecondActivityForResult(EditInfoFragment.this, SignatureFragment.class, signature, SignatureFragment.TAG_SIGNATURE_FRAGMENT, REQUEST_SIGNATURE);
                        break;
                }
            }
        });

    }

    private View.OnClickListener editAcatarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_a_picture:
                    break;
                case R.id.select_from_album:
                    break;
                case R.id.opt_cancel:
                    if (photoSheetDialog != null) {
                        photoSheetDialog.dismiss();
                    }
                    break;
            }

        }
    };

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
    public void loadInfoSuccess(Info info) {
        if (info != null) {
            adapter.setData(info);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInfoFailure(String msg) {

    }
}
