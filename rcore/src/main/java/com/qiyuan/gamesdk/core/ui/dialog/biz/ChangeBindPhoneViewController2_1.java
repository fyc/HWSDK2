package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

/**
 * 更换绑定手机页面1
 */
public class ChangeBindPhoneViewController2_1 extends BaseAuthViewController {
    Context mContext;
    ContainerItemTitle4 containerItemTitle4;
    TextView tv_phone_account;

    public ChangeBindPhoneViewController2_1(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_change_bind_phone2_1;
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
