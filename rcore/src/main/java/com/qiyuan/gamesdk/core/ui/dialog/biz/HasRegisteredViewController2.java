package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemBottom2;
import com.qygame.qysdk.outer.event.IDialogParam;

public class HasRegisteredViewController2 extends BaseAuthViewController {

    public Context context;
    ImageView img_head;
    TextView tv_user_name;
    Button btn_enter_game;
    TextView tv_to_register;

    ContainerItemBottom2 containerItemBottom2;
//    public View loginPhoneButton; //跳转至手机登录界面
//    public View loginAccountButton; //跳转至账号登录界面
//    public View backRegisterButton; //跳转至注册

    public HasRegisteredViewController2(Context context, IDialogParam params) {
        super(context, params);
        this.context = context;
        initView();
    }

    private void initView() {
        img_head = (ImageView) findViewById(R.id.img_head);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        btn_enter_game = (Button) findViewById(R.id.btn_has_registered_container_enter_game);
        tv_to_register = (TextView) findViewById(R.id.tv_to_register);

//        loginPhoneButton = findViewById(R.id.btn_qiyuan_phone_login);
//        loginAccountButton = findViewById(R.id.btn_qiyuan_account_login);
//        backRegisterButton = findViewById(R.id.btn_back_to_regist);
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

//        loginPhoneButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
//            }
//        });
//        loginAccountButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
//            }
//        });
//        backRegisterButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                IMEUtil.hideIME(HasRegisteredViewController2.this);
//                ViewControllerNavigator.getInstance().toRegister2(getDialogParam());
//            }
//        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_has_registered;
    }

}
