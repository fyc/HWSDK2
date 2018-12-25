package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

public class AccountCenterViewController2 extends BaseAuthViewController {
    private static final String TAG = "QYSDK:UserAccountViewController2";
    Context mContext;
    ContainerItemTitle4 containerItemTitle4;
    RelativeLayout rl_change_password, rl_bind_phone, rl_real_name_auth, rl_pay_record;

    public AccountCenterViewController2(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_account_center2;
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_user_account);
        containerItemTitle4.setTitleBtnVisibility(true,true,true);
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

        rl_change_password = (RelativeLayout) findViewById(R.id.rl_change_password);
        rl_bind_phone = (RelativeLayout) findViewById(R.id.rl_bind_phone);
        rl_real_name_auth = (RelativeLayout) findViewById(R.id.rl_real_name_auth);
        rl_pay_record = (RelativeLayout) findViewById(R.id.rl_pay_record);

        rl_change_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().toResetPassword2();
            }
        });
        rl_bind_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().toBindPhone2();
            }
        });
        rl_bind_phone.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ViewControllerNavigator.getInstance().toChangeBindPhone2_1();
                return false;
            }
        });
        rl_real_name_auth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().toRealNameAuth2();
            }
        });
        rl_pay_record.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
