package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

public class BindPhoneSuccessfulViewController2 extends BaseAuthViewController {
    Context mContext;
    ContainerItemTitle4 containerItemTitle4;
    TextView tv_phone_account;

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
        tv_phone_account = (TextView) findViewById(R.id.tv_phone_account);
    }
}
