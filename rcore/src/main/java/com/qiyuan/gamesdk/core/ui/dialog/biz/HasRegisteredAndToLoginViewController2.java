package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemBottom2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

public class HasRegisteredAndToLoginViewController2 extends BaseAuthViewController {

    public Context context;
    ContainerItemTitle4 containerItemTitle4;
    ImageView img_head;
    TextView tv_user_name;
    Button btn_enter_game;
    TextView tv_to_register;

    ContainerItemBottom2 containerItemBottom2;

    public HasRegisteredAndToLoginViewController2(Context context, IDialogParam params) {
        super(context, params);
        this.context = context;
        initView();
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_qiyuan_sdk_game_title);
        containerItemTitle4.setTitleBtnVisibility(true,false,true);
        containerItemTitle4.setTitleOnclick(new ContainerItemTitle4.TitleOnclick() {
            @Override
            public void toBack() {
                ViewControllerNavigator.getInstance().toRegister2(getDialogParam());
            }

            @Override
            public void toRefresh() {

            }

            @Override
            public void toClose() {
                close();
            }
        });
        img_head = (ImageView) findViewById(R.id.img_head);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        btn_enter_game = (Button) findViewById(R.id.btn_has_registered_container_enter_game);
        tv_to_register = (TextView) findViewById(R.id.tv_to_register);
        containerItemBottom2 = (ContainerItemBottom2) findViewById(R.id.containerItemBottom2);
        containerItemBottom2.setBaseAuthViewController(this);
        containerItemBottom2.setBtnVisibility(false, true, true);
        tv_to_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                IMEUtil.hideIME(HasRegisteredViewController2.this);
                ViewControllerNavigator.getInstance().toRegister2(getDialogParam());
            }
        });

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_has_registered_and_to_login;
    }

}
