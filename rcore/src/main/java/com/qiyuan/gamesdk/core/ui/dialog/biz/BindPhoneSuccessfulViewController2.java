package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

public class BindPhoneSuccessfulViewController2 extends BaseAuthViewController {
    Context mContext;
    ContainerItemTitle4 containerItemTitle4;
    Button btn_return_account_center;

    public BindPhoneSuccessfulViewController2(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_bind_phone_successful2;
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_bind_phone_title2);
        containerItemTitle4.setBackTitleBtnVisibility(View.GONE);
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
                ViewControllerNavigator.getInstance().toUserAccount2();
            }
        });
    }
}
