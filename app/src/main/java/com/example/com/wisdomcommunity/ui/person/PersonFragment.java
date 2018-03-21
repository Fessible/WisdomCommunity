package com.example.com.wisdomcommunity.ui.person;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.wisdomcommunity.mvp.InfoContract;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.login.LoginFragment;
import com.example.com.wisdomcommunity.ui.person.address.AddressFragment;
import com.example.com.wisdomcommunity.ui.person.feedback.FeedbackFragment;
import com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment;
import com.example.com.wisdomcommunity.ui.person.service.ServiceFragment;
import com.example.com.wisdomcommunity.ui.person.set.SetFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_ADDRESS;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_EXIT;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_FEEDBACK;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_SERVICE;
import static com.example.com.wisdomcommunity.ui.person.PersonAdapter.TYPE_SET;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_HEADIMAGE;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_NAME;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoFragment.KEY_SIGNATURE;

/**
 * 个人中心
 * Created by rhm on 2018/1/16.
 */

public class PersonFragment extends BaseFragment implements InfoContract.View {
    public static final String TAG_PERSON_FRAGMENT = "PERSON_FRAGMENT";
    public static final String KEY_INFO = "INFO";

    public static final int REQUEST_CODE = 2;
    private Info info;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.head_image)
    CircleImageView headImage;

    @BindView(R.id.nick_name)
    TextView nickName;

    @BindView(R.id.signature)
    TextView signature;

    private PersonAdapter adapter;
    private InfoContract.Presenter infoPresenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_person_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        infoPresenter = new PersonPresenter(getContext(), PersonFragment.this);
        //todo userId
        infoPresenter.loadInfo("122");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        adapter.setOnItemClickListener(new PersonAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int type) {
                switch (type) {
                    case TYPE_ADDRESS:
                        IntentUtil.startTemplateActivity(PersonFragment.this, AddressFragment.class, AddressFragment.TAG_ADDRESS_FRAGMENT);
                        break;
                    case TYPE_FEEDBACK:
                        IntentUtil.startTemplateActivity(PersonFragment.this, FeedbackFragment.class, FeedbackFragment.TAG_FEEDBACK_FRAGMENT);
                        break;
                    case TYPE_SERVICE:
                        IntentUtil.startTemplateActivity(PersonFragment.this, ServiceFragment.class, ServiceFragment.TAG_SERVICE_FRAGMENT);
                        break;
                    case TYPE_SET:
                        IntentUtil.startTemplateActivity(PersonFragment.this, SetFragment.class, SetFragment.TAG_SET_FRAGMENT);
                        break;
                    case TYPE_EXIT:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.title_tips)
                                .setMessage(R.string.exit_messgae)
                                .setPositiveButton(R.string.opt_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signOut();
                                    }
                                })
                                .setNegativeButton(R.string.opt_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show()
                                .setCanceledOnTouchOutside(false);
                        break;
                }
            }
        });
    }


    private void signOut() {
        getActivity().finish();
        IntentUtil.startTemplateActivity(PersonFragment.this, LoginFragment.class, LoginFragment.TAG_Login_FRAGMENT);
//        if (logoutPresenter != null) {
//            logoutPresenter.logout();
//        }
    }

    @OnClick(R.id.person_layout)
    public void editInfo() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("INFO", info);
        IntentUtil.startTemplateActivityForResult(PersonFragment.this, EditInfoFragment.class, bundle, EditInfoFragment.TAG_INFO_FRAGMENT, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    signature.setText(data.getStringExtra(KEY_SIGNATURE));
                    nickName.setText(data.getStringExtra(KEY_NAME));
                    final int placeholderResId = R.drawable.icon_head_address;
                    Glide.with(getContext())
                            .load(data.getStringExtra(KEY_HEADIMAGE))
                            .apply(new RequestOptions().placeholder(placeholderResId).centerCrop().fallback(placeholderResId))
                            .into(headImage);
                    break;
            }
        }
    }

    @Override
    protected void destroyView() {
        adapter.destroy();
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
        final int placeholderResId = R.drawable.icon_head_address;
        if (info != null) {
            this.info = info;
            Glide.with(getContext())
                    .load(info.headImage)
                    .apply(new RequestOptions().placeholder(placeholderResId).centerCrop().fallback(placeholderResId))
                    .into(headImage);
            nickName.setText(info.userName);
            signature.setText(info.signature);
        }

    }

    @Override
    public void loadInfoFailure(String msg) {

    }
}
