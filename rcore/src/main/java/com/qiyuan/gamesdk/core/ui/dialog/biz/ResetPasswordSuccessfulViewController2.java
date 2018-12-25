package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

public class ResetPasswordSuccessfulViewController2 extends BaseAuthViewController {
    Context mContext;
    ContainerItemTitle4 containerItemTitle4;
    Button btn_return_account_center;

    public ResetPasswordSuccessfulViewController2(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_reset_password_successful2;
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.account_reset_passwd);
        containerItemTitle4.setTitleBtnVisibility(false,false,true);
        containerItemTitle4.setTitleOnclick(new ContainerItemTitle4.TitleOnclick() {
            @Override
            public void toBack() {
            }

            @Override
            public void toRefresh() {
            }

            @Override
            public void toClose() {
                close();
            }
        });
        btn_return_account_center = (Button) findViewById(R.id.btn_return_account_center);
        btn_return_account_center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().toAccountCenter2();
            }
        });
    }
}
