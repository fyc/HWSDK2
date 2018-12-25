package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter.LoginViewControllerPresenter2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemBottom2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qiyuan.gamesdk.core.ui.widget.DrawableEditText;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;

/**
 * Created by Orange on 15/6/10.
 */
public class LoginViewController2 extends BaseAuthViewController {

    public static final String TAG = "QYSDK:LoginViewController2 ";
    private static final String MD5_PWD_PREFIX = "\u0d62\u0d63\u135f";
//    public static final int STATE_LOGIN_PHONE = 1;
//    public static final int STATE_LOGIN_ACCOUNT = 2;
    public Context context;
    ContainerItemTitle4 containerItemTitle4;
    LoginViewControllerPresenter2 loginViewControllerPresenter2;
    public int currentState;
    public TextView title_tip;
    public Button loginButton; //登录按钮

    ContainerItemBottom2 containerItemBottom2;

    public EditText accountEdit;
    public EditText passwordEdit;

    //自动填账号/手机号 缓存.
    private String autoFillCache = "";
    //data
    public String requestingPhoneNumber;
    public String requestingPhoneMd5Pwd;
    public String requestingAccount;
    public String requestingAccountMd5Pwd;

    public LoginViewController2(Context context, IDialogParam params) {
        super(context, params);
        this.context = context;
        loginViewControllerPresenter2 = new LoginViewControllerPresenter2(this);
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_login2;
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_qiyuan_sdk_game_title);
        containerItemTitle4.setTitleBtnVisibility(false,false,false);
        loginButton = (Button) findViewById(R.id.btn_login_container_login);
        ViewUtils.setViewEnable(loginButton, false);
        title_tip = (TextView) findViewById(R.id.qy_sdk_container_item_sub_title);

//        loginPhoneButton = findViewById(R.id.btn_qiyuan_phone_login);
//        loginAccountButton = findViewById(R.id.btn_qiyuan_account_login);
//        backRegisterButton = findViewById(R.id.btn_back_to_regist);
        containerItemBottom2 = (ContainerItemBottom2) findViewById(R.id.containerItemBottom2);
        containerItemBottom2.setBaseAuthViewController(this);
        containerItemBottom2.setBtnVisibility(false, true, true);

        accountEdit = (DrawableEditText) findViewById(R.id.edit_login_container_account);
        passwordEdit = (EditText) findViewById(R.id.edit_login_container_account_password);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IMEUtil.hideIME(LoginViewController2.this);
//                if (currentState == STATE_LOGIN_PHONE) {
//                    if (loginViewControllerPresenter2.checkPhoneInput(phoneEdit.getText().toString(), phonePasswordEdit.getText().toString())) {
//                        loginViewControllerPresenter2.loginByPhoneImpl(requestingPhoneNumber, requestingPhoneMd5Pwd);
//                    }
//                } else if (currentState == STATE_LOGIN_ACCOUNT) {
//                    if (loginViewControllerPresenter2.checkAccountInput(accountEdit.getText().toString(), accountPasswordEdit.getText().toString()))
//                        loginViewControllerPresenter2.loginByAccountImpl(requestingAccount, requestingAccountMd5Pwd);
//                }
            }
        });
//        loginPhoneButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchLoginTypeState(STATE_LOGIN_PHONE);
//            }
//        });
//        loginAccountButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchLoginTypeState(STATE_LOGIN_ACCOUNT);
//            }
//        });
//        backRegisterButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IMEUtil.hideIME(LoginViewController2.this);
//                ViewControllerNavigator.getInstance().toRegister2(getDialogParam());
//            }
//        });

        loginViewControllerPresenter2.addTextWatcher(accountEdit, passwordEdit);
    }


    public void onLoginFail() {
        hideLoading();
    }

    public void onLoginSuccess(AuthModel result) {
        hideLoading();
        notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
        close();
    }

    private void cleanInputs() {
        accountEdit.setText("");
    }


    private void updateLoginButtonState() {
        if (accountEdit.length() == 0 || passwordEdit.length() == 0
                ) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }
    }
}
