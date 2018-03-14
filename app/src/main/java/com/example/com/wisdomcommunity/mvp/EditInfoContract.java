package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.personal.HeadImage;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.params.PersonParams;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

import java.io.File;

/**
 * Created by rhm on 2018/2/28.
 */

public interface EditInfoContract extends BasicContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void loadInfoSuccess(Info info);

        void loadInfoFailure(String msg);

        void editHeadImageSuccess(HeadImage url, String msg);

        void editHeadImageFailure(String msg);

        void editInfoSuccess(String msg);

        void editInfoFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void editHeadImage(File headImage);

        public abstract void editInfo(PersonParams info);
    }

}
