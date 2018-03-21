package com.example.com.wisdomcommunity.ui.person.info;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.personal.HeadImage;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.params.PersonParams;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.EditInfoContract;
import com.example.com.wisdomcommunity.ui.person.info.signature.SignatureFragment;
import com.example.com.wisdomcommunity.ui.person.info.username.UserNameFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.util.photo.PathUtils;
import com.example.com.wisdomcommunity.util.photo.PhotosClient;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.io.File;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.person.PersonFragment.KEY_INFO;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_BACK;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_DISTRICT;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_HEAD_IMAGE;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_NAME;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_SEX;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.TYPE_SIGNATURE;
import static com.example.com.wisdomcommunity.ui.person.info.username.UserNameFragment.KEY_USER_NAME;

/**
 * Created by rhm on 2018/2/28.
 */

public class EditInfoFragment extends BaseFragment implements EditInfoContract.View {
    public static final String TAG_INFO_FRAGMENT = "INFO_FRAGMENT";
    public static final int REQUEST_NAME = 0;
    public static final int REQUEST_SIGNATURE = 1;
    public static final String KEY_NAME = "user_name";
    public static final String KEY_SIGNATURE = "signature";
    public static final String KEY_HEADIMAGE = "headImage";
    private BottomSheetDialog photoSheetDialog;
    private BottomSheetDialog sexBottomDialog;

    private static final int REQUEST_CODE_TAKE_A_PICTURE = 10086;
    private static final int REQUEST_CODE_SELECT_FROM_ALBUM = 10087;
    private static final int REQUEST_CODE_CROP_PICTURE = 10088;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private EditInfoAdapter adapter;
    private EditInfoContract.Presenter presenter;
    private PhotosClient photosClient;
    private int headPosition = -1;
    private int namePosition = -1;
    private int signaturePosition = -1;
    private PersonParams personParams = new PersonParams();
    private String headImageUrl;

    @Override
    public int getResLayout() {
        return R.layout.fragment_edit_person;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        adapter = new EditInfoAdapter(getContext());
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        recyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Info info = (Info) bundle.getSerializable(KEY_INFO);
            if (info != null) {
                headImageUrl = info.headImage;
                personParams.districtName = info.districtName;
                personParams.nickName = info.userName;
                personParams.sex = info.sex;
                personParams.signature = info.signature;
                adapter.setData(info);
            }
        }

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setClickListener(new EditInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int type, String content, final int position) {
                switch (type) {
                    case TYPE_HEAD_IMAGE:
                        headPosition = position;
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
                        namePosition = position;
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_NAME, content);
                        IntentUtil.startTemplateActivityForResult(EditInfoFragment.this, UserNameFragment.class, bundle, UserNameFragment.TAG_USER_NAME_FRAGMENT, REQUEST_NAME);
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
                                    sexBottomDialog.dismiss();
                                    if (adapter != null) {
                                        adapter.setValue(getContext().getString(R.string.men), position);
                                    }
                                }
                            });
                            TextView women = sexBottomDialog.findViewById(R.id.select_from_album);
                            women.setText(getContext().getString(R.string.woman));
                            women.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sexBottomDialog.dismiss();
                                    if (adapter != null) {
                                        adapter.setValue(getContext().getString(R.string.woman), position);
                                    }
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
                        signaturePosition = position;
                        Bundle signature = new Bundle();
                        signature.putString(KEY_SIGNATURE, content);
                        IntentUtil.startTemplateActivityForResult(EditInfoFragment.this, SignatureFragment.class, signature, SignatureFragment.TAG_SIGNATURE_FRAGMENT, REQUEST_SIGNATURE);
                        break;
                    case TYPE_BACK:
                        if (presenter != null) {
                            presenter.editInfo(personParams);
                        }
                        Intent intent = new Intent();
                        intent.putExtra(KEY_NAME, personParams.nickName);
                        intent.putExtra(KEY_SIGNATURE, personParams.signature);
                        intent.putExtra(KEY_HEADIMAGE, headImageUrl);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new EditInfoPresenter(getContext(), EditInfoFragment.this);

        photosClient = new PhotosClient.Builder(this)
                .cropSize(200)
                .cropGalleryDir(PathUtils.getGalleryDir(getContext()))
                .requestCodeTakeAPicture(REQUEST_CODE_TAKE_A_PICTURE)
                .requestCodeSelectFromAlbum(REQUEST_CODE_SELECT_FROM_ALBUM)
                .requestCodeCropPicture(REQUEST_CODE_CROP_PICTURE)
                .callback(photosClientCallback)
                .build();
    }

    private PhotosClient.Callback photosClientCallback = new PhotosClient.Callback() {
        @Override
        public void onCropPicture(Uri pictureUri) {
            headImageUrl = pictureUri.getPath();
            if (adapter != null && presenter != null) {
                adapter.setImage(headImageUrl, headPosition);
                presenter.editHeadImage(new File(pictureUri.getPath()));
            }
        }
    };
    private View.OnClickListener editAcatarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_a_picture:
                    if (photosClient != null) {
                        photosClient.takeAPicture();
                    }
                    if (photoSheetDialog != null) {
                        photoSheetDialog.dismiss();
                    }
                    break;
                case R.id.select_from_album:
                    if (photosClient != null) {
                        photosClient.selectFromAlbum();
                    }
                    if (photoSheetDialog != null) {
                        photoSheetDialog.dismiss();
                    }
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
        if (adapter != null) {
            adapter.destroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_NAME:
                    if (data != null) {
                        String name = data.getStringExtra(KEY_USER_NAME);
                        personParams.nickName = name;
                        adapter.setValue(name, namePosition);
                    }
                    break;
                case REQUEST_SIGNATURE:
                    if (data != null) {
                        String signature = data.getStringExtra(KEY_SIGNATURE);
                        personParams.signature = signature;
                        adapter.setValue(signature, signaturePosition);
                    }
                    break;
                default:
                    if (photosClient != null && photosClient.onActivityResult(requestCode, resultCode, data)) {
                        return;
                    }
                    break;
            }
        }
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

    }

    @Override
    public void loadInfoFailure(String msg) {

    }

    @Override
    public void editHeadImageSuccess(HeadImage headImage, String msg) {
//        headImageUrl = headImage.avatarURL;
    }

    @Override
    public void editHeadImageFailure(String msg) {
        showShortToast(msg);
    }

    @Override
    public void editInfoSuccess(String msg) {
        showShortToast(msg);
    }

    @Override
    public void editInfoFailure(String msg) {
        showShortToast(msg);
    }

//    @Override
//    public void loadInfoSuccess(Info info) {
//        if (info != null) {
//            adapter.setData(info);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void loadInfoFailure(String msg) {
//
//    }
}
