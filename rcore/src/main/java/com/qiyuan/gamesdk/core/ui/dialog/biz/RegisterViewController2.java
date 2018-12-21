package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter.RegisterViewControllerPresenter2;
import com.qiyuan.gamesdk.core.ui.widget.StandardDialog;
import com.qiyuan.gamesdk.model.UserInfo;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qygame.qysdk.outer.util.StringUtils;

import java.util.Stack;

/**
 * Created by Orange on 15/6/11.
 * 注册，手机号注册，一键注册
 */
public class RegisterViewController2 extends BaseAuthViewController {
    public static final String TAG = "QYSDK:RegisterViewController ";

    public static final int STATE_REGISTER_PHONE = 1;
    public static final int STATE_REGISTER_ACCOUNT = 2;

    RegisterViewControllerPresenter2 registerViewControllerPresenter2;
    public boolean waitingVerifyCode = false;

    public int currentState;
    public int retryTime = 0;

    //header
    private ViewGroup registerTypeRadioGroup;
    private RadioButton registerPhoneRadioButton;
    private RadioButton registerAccountRadioButton;

    public View registerPhoneContainer;
    public View registerAccountContainer;
    public EditText phoneEdit;
    public EditText phonePasswordEdit;
    //    private EditText accountEdit;
    public EditText accountPasswordEdit;
    public Button getVerificationCodeButton;
    //    private Button backTitleContainerBtn;
//    private Button closeTitleContainerBtn;
    public EditText verificationCodeEdit;
    public Button registerButton;

    private View registerPhoneBottom;
    private View registerAccountBottom;
    public View loginPhoneButton; //跳转至手机登录界面
    public View loginAccountButton; //跳转至账号登录界面
    public View backRegisterButton; //跳转至注册

    private TextView titleTv;
    private TextView tv_phone_account, tv_qiyuan_account;
    public ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
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

    }

    @Override
    public void close() {
        super.close();
        //注销短信监听器
        mContext.getContentResolver().unregisterContentObserver(smsObserver);
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.tv_title_container_title);
//        titleImg = findViewById(R.id.img_title_container_title);
//        serviceTermsBtn = (TextView)findViewById(R.id.tv_service_terms);
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
//        accountEdit = (EditText) findViewById(R.id.edit_register_container_account);
        accountPasswordEdit = (EditText) findViewById(R.id.edit_register_container_account_password);
        reGetVerifyCodeButtonController = new ReGetVerifyCodeButtonController(getVerificationCodeButton);
        verificationCodeEdit = (EditText) findViewById(R.id.edit_register_container_verification_code);
//        backTitleContainerBtn = (Button)findViewById(R.id.btn_title_container_back);
//        closeTitleContainerBtn = (Button)findViewById(R.id.btn_title_container_close);
        tv_phone_account = (TextView) findViewById(R.id.tv_phone_account);
        tv_qiyuan_account = (TextView) findViewById(R.id.tv_qiyuan_account);
        registerPhoneBottom = findViewById(R.id.container_item_register_phone_bottom);
        registerAccountBottom = findViewById(R.id.container_item_register_account_bottom);

        loginPhoneButton = findViewById(R.id.btn_qiyuan_phone_login);
        loginAccountButton = findViewById(R.id.btn_qiyuan_account_login);
        backRegisterButton = findViewById(R.id.btn_back_to_regist);
        backRegisterButton.setVisibility(View.GONE);

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

        getVerificationCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneEdit.getText().toString();
                String pwd = phonePasswordEdit.getText().toString();
                if (StringUtils.isBlank(phone)) {
                    ToastUtils.showMsg(ResourceHelper.getString(R.string.phone_blank));
                    return;
                }
                if (phone.length() != 11) {
                    ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_11_phone_number));
                    return;
                }
                if (!phone.startsWith("1") && !phone.startsWith("9")) {
                    ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_valid_number));
                    return;
                }
                IMEUtil.hideIME(RegisterViewController2.this);
                //获取验证码
                registerViewControllerPresenter2.getVerificationCodeButtonImpl(phone);
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

        phoneEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ViewUtils.setViewEnable(getVerificationCodeButton, phoneEdit.length() != 0);
            }
        });
        ViewUtils.bindEditWithButton(accountPasswordEdit, registerButton);
        ViewUtils.bindEditWithButton(phonePasswordEdit, registerButton);

        registerViewControllerPresenter2.addTextWatcher(phoneEdit, phonePasswordEdit, accountPasswordEdit, verificationCodeEdit);

        tv_phone_account.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
            }
        });
        tv_qiyuan_account.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
            }
        });

        loginPhoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
            }
        });
        loginAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewControllerNavigator.getInstance().tologinPhone2(getDialogParam());
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        boolean blockBackAction = waitingVerifyCode;
        if (waitingVerifyCode) {
            IMEUtil.hideIME(RegisterViewController2.this);
            warningExitRegister(new TwoChoiceJob() {
                @Override
                public void jobOnChoose() {
                    waitingVerifyCode = false;
                    close();
                }

                @Override
                public void jobDefault() {

                }
            });
        }
        return blockBackAction;
    }

    private interface TwoChoiceJob {
        void jobOnChoose();

        void jobDefault();
    }

    private void warningLeaveRegister(final TwoChoiceJob job) {
        if (waitingVerifyCode) {
            StandardDialog dialog
                    = new StandardDialog(getDialogParam().getActivityContext());
            dialog.setMessageTip(R.string.warning_back_to_register);
            dialog.setEnsureText(R.string.restart);
            dialog.setCancelText(R.string.keep_waiting);
            dialog.setListener(new StandardDialog.DialogClickListener() {
                @Override
                public void onEnsureClick() {
                    verificationCodeEdit.setText("");
                    reGetVerifyCodeButtonController.cancelCountDown();
                    job.jobOnChoose();
                }
            });
            dialog.show();
        } else {
            job.jobDefault();
        }
    }

    private void warningExitRegister(final TwoChoiceJob job) {
        if (waitingVerifyCode) {
            StandardDialog dialog
                    = new StandardDialog(getDialogParam().getActivityContext());
            dialog.setMessageTip(R.string.warning_back_to_register);
            dialog.setEnsureText(R.string.yes_i_wanna_exit);
            dialog.setCancelText(R.string.keep_waiting);
            dialog.setListener(new StandardDialog.DialogClickListener() {
                @Override
                public void onEnsureClick() {
                    verificationCodeEdit.setText("");
                    reGetVerifyCodeButtonController.cancelCountDown();
                    job.jobOnChoose();
                }
            });
            dialog.show();
        } else {
            job.jobDefault();
        }
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
                registerAccountBottom.setVisibility(View.GONE);
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
                registerAccountBottom.setVisibility(View.VISIBLE);
                registerButton.setText(R.string.str_complete_the_registration);
                break;
            default:
                return;
        }
        currentState = state;
        registerViewControllerPresenter2.updateRegisterButtonState();
    }

}
