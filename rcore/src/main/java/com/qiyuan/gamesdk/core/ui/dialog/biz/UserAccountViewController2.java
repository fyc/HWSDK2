package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;

import com.qiyuan.gamesdk.R;
import com.qygame.qysdk.outer.event.IDialogParam;

public class UserAccountViewController2 extends BaseAuthViewController {
    public UserAccountViewController2(Context context, IDialogParam params) {
        super(context, params);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_user_account2;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
