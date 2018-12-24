package com.qiyuan.gamesdk.core.ui.dialog.biz.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qiyuan.gamesdk.BuildConfig;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.BaseAuthViewController;
import com.qiyuan.gamesdk.util.IMEUtil;

public class ContainerItemBottom2 extends RelativeLayout {

    private Button btn_to_login;
    private Button btn_to_forget_password;
    private Button btn_back_to_regist;
    BaseAuthViewController baseAuthViewController;

    public ContainerItemBottom2(Context context) {
        this(context, null);
    }

    public ContainerItemBottom2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerItemBottom2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (BuildConfig.isApp) {
            LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.qy_sdk_container_item_bottom2, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.qy_sdk_container_item_bottom2, this);
        }

        initView();
    }

    public void setBaseAuthViewController(BaseAuthViewController baseAuthViewController) {
        this.baseAuthViewController = baseAuthViewController;
    }

    private void initView() {
        btn_to_login = (Button) findViewById(R.id.btn_to_login);
        btn_to_forget_password = (Button) findViewById(R.id.btn_to_forget_password);
        btn_back_to_regist = (Button) findViewById(R.id.btn_back_to_regist);
        btn_to_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().tologinPhone2(baseAuthViewController.getDialogParam());
            }
        });
        btn_to_forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewControllerNavigator.getInstance().tologinPhone2(baseAuthViewController.getDialogParam(), LoginViewController2.STATE_LOGIN_PHONE);
            }
        });
        btn_back_to_regist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtil.hideIME(baseAuthViewController);
                ViewControllerNavigator.getInstance().toRegister2(baseAuthViewController.getDialogParam());
            }
        });
    }

    /**顺序为：
     * 登录、忘记密码、返回注册
    * */
    public void setBtnVisibility(boolean isVisibility1, boolean isVisibility2, boolean isVisibility3) {
        btn_to_login.setVisibility(isVisibility1 ? View.VISIBLE : View.GONE);
        btn_to_forget_password.setVisibility(isVisibility2 ? View.VISIBLE : View.GONE);
        btn_back_to_regist.setVisibility(isVisibility3 ? View.VISIBLE : View.GONE);
    }

}
