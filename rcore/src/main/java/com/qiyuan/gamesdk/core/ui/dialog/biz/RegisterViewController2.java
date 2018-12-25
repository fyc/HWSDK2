package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter.GetVerifyCodePresenter2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter.RegisterViewControllerPresenter2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemBottom2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qiyuan.gamesdk.model.UserInfo;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;

import java.util.Stack;

/**
 * Created by Orange on 15/6/11.
 * 注册，手机号注册，一键注册
 */
public class RegisterViewController2 extends BaseAuthViewController {
    public static final String TAG = "QYSDK:RegisterViewController ";

    public static final int STATE_REGISTER_PHONE = 1;
    public static final int STATE_REGISTER_ACCOUNT = 2;

    ContainerItemTitle4 containerItemTitle4;
    RegisterViewControllerPresenter2 registerViewControllerPresenter2;
//    public boolean waitingVerifyCode = false;

    public int currentState;

    //header
    private ViewGroup registerTypeRadioGroup;
    private RadioButton registerPhoneRadioButton;
    private RadioButton registerAccountRadioButton;

    public View registerPhoneContainer;
    public View registerAccountContainer;
    public EditText phoneEdit;
    public EditText phonePasswordEdit;
    public Button getVerificationCodeButton;
    public EditText verificationCodeEdit;

    GetVerifyCodePresenter2 getVerifyCodePresenter2;//验证码助手类

    //    private EditText accountEdit;
    public EditText accountPasswordEdit;

    public Button registerButton;
    private View registerPhoneBottom;
    ContainerItemBottom2 containerItemBottom2;

//    private TextView titleTv;
    //    private TextView tv_phone_account, tv_qiyuan_account;
    private TextView btn_to_login;
    private TextView btn_to_forget_password;
    //    public ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    public RegisterViewControllerPresenter2.SmsVerificationCode smsObserver;


    //data
    private String requestingPhoneNumber;
    private String requestingPhoneMd5Pwd;
    private String requestingAccount;
    private String requestingAccountMd5Pwd;
    private Stack<UserInfo> registerUserCache = new Stack<>();

    public Context mContext;

    public RegisterViewController2(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        registerViewControllerPresenter2 = new RegisterViewControllerPresenter2(this);
        initView();
        switchRegisterTypeState(STATE_REGISTER_PHONE);
        //注册短信变化监听
        smsObserver = registerViewControllerPresenter2.newHistoryPickerController(new Handler());
        mContext.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_register2;
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
//        reGetVerifyCodeButtonController.cancelCountDown();
        getVerifyCodePresenter2.cancelCountDown();

    }

    @Override
    public void close() {
        super.close();
        //注销短信监听器
        mContext.getContentResolver().unregisterContentObserver(smsObserver);
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_qiyuan_sdk_game_title);
        containerItemTitle4.setTitleBtnVisibility(false,false,false);
        registerTypeRadioGroup = (ViewGroup) findViewById(R.id.radio_group_item_register_type);
        registerPhoneRadioButton = (RadioButton) findViewById(R.id.radio_item_register_phone);
        registerPhoneRadioButton.setSelected(true);
        registerAccountRadioButton = (RadioButton) findViewById(R.id.radio_item_register_account);
        registerPhoneContainer = findViewById(R.id.container_item_register_phone);
        registerAccountContainer = findViewById(R.id.container_item_register_account);
        getVerificationCodeButton = (Button) findViewById(R.id.btn_register_container_get_verification_code);
        ViewUtils.setViewEnable(getVerificationCodeButton, false);
        registerButton = (Button) findViewById(R.id.btn_register_container_register);
        ViewUtils.setViewEnable(registerButton, false);
        phoneEdit = (EditText) findViewById(R.id.edit_register_container_phone);
        phonePasswordEdit = (EditText) findViewById(R.id.edit_register_container_password);
        verificationCodeEdit = (EditText) findViewById(R.id.edit_register_container_verification_code);
        getVerifyCodePresenter2 = new GetVerifyCodePresenter2(this, phoneEdit, verificationCodeEdit, getVerificationCodeButton);

//        accountEdit = (EditText) findViewById(R.id.edit_register_container_account);
        accountPasswordEdit = (EditText) findViewById(R.id.edit_register_container_account_password);

        btn_to_login = (TextView) findViewById(R.id.btn_to_login);
        btn_to_forget_password = (TextView) findViewById(R.id.btn_to_forget_password);

        registerPhoneBottom = findViewById(R.id.container_item_register_phone_bottom);
        containerItemBottom2 = (ContainerItemBottom2) findViewById(R.id.containerItemBottom2);
        containerItemBottom2.setBaseAuthViewController(this);
        containerItemBottom2.setBtnVisibility(true, true, false);

        registerAccountRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!registerAccountRadioButton.isSelected()) {
                    switchRegisterTypeState(STATE_REGISTER_ACCOUNT);
                }
            }
        });

        registerPhoneRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!registerPhoneRadioButton.isSelected()) {
                    registerPhoneRadioButton.setSelected(true);
                    switchRegisterTypeState(STATE_REGISTER_PHONE);
                }
            }
        });


        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IMEUtil.hideIME(RegisterViewController2.this);
                if (currentState == STATE_REGISTER_PHONE) {
                    if (registerViewControllerPresenter2.checkPhoneInput(phoneEdit.getText().toString(), phonePasswordEdit.getText().toString())) {
                        registerViewControllerPresenter2.registerByPhoneImpl(requestingPhoneNumber, requestingPhoneMd5Pwd, verificationCodeEdit.getText().toString());
                    }
                } else if (currentState == STATE_REGISTER_ACCOUNT) {
                    if (registerViewControllerPresenter2.checkAccountInput(accountPasswordEdit.getText().toString()))
                        registerViewControllerPresenter2.registerByAccountImpl(requestingAccount, requestingAccountMd5Pwd);
                }
            }
        });

        ViewUtils.bindEditWithButton(accountPasswordEdit, registerButton);
        ViewUtils.bindEditWithButton(phonePasswordEdit, registerButton);

        registerViewControllerPresenter2.addTextWatcher(phoneEdit, phonePasswordEdit, accountPasswordEdit, verificationCodeEdit);

        btn_to_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
            }
        });
        btn_to_forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().toRetrievePasswordViewController2(getDialogParam());
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return getVerifyCodePresenter2.onBackPressed();
    }


    private void clearRegisterPhoneInfo() {
        requestingPhoneNumber = "";
        requestingPhoneMd5Pwd = "";
    }

    public void putRegisterPhoneInfo(String phone, String md5Pwd) {
        requestingPhoneNumber = phone;
        requestingPhoneMd5Pwd = md5Pwd;
    }

    public void putRegisterAccountInfo(String account, String md5Pwd) {
        requestingAccount = account;
        requestingAccountMd5Pwd = md5Pwd;
    }

    private void switchRegisterTypeState(int state) {
        if (currentState == state) {
            return;
        }
        switch (state) {
            case STATE_REGISTER_PHONE:
                registerPhoneContainer.setVisibility(View.VISIBLE);
                registerAccountContainer.setVisibility(View.GONE);
                registerPhoneRadioButton.setSelected(true);
                registerAccountRadioButton.setSelected(false);
                registerPhoneRadioButton
                        .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                                R.drawable.qy_sdk_shape_dialog_radio_button_bg2);
                registerAccountRadioButton
                        .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                                R.drawable.qy_sdk_shape_dialog_radio_button_bg_empty);

                registerPhoneBottom.setVisibility(View.VISIBLE);
                containerItemBottom2.setVisibility(View.GONE);
                registerButton.setText(R.string.str_enter_game);
                break;
            case STATE_REGISTER_ACCOUNT:
                registerPhoneContainer.setVisibility(View.GONE);
                registerAccountContainer.setVisibility(View.VISIBLE);
                registerPhoneRadioButton.setSelected(false);
                registerAccountRadioButton.setSelected(true);
                registerPhoneRadioButton
                        .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                                R.drawable.qy_sdk_shape_dialog_radio_button_bg_empty);
                registerAccountRadioButton
                        .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                                R.drawable.qy_sdk_shape_dialog_radio_button_bg2);

                registerPhoneBottom.setVisibility(View.GONE);
                containerItemBottom2.setVisibility(View.VISIBLE);
                registerButton.setText(R.string.str_complete_the_registration);
                break;
            default:
                return;
        }
        currentState = state;
        registerViewControllerPresenter2.updateRegisterButtonState();
    }

}
