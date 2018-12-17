package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter.LoginViewControllerPresenter2;
import com.qiyuan.gamesdk.core.ui.widget.DrawableEditText;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.StringUtils;

import java.util.List;

/**
 * Created by Orange on 15/6/10.
 */
public class LoginViewController2 extends BaseAuthViewController {

    public static final String TAG = "QYSDK:LoginViewController2 ";
    private static final String MD5_PWD_PREFIX = "\u0d62\u0d63\u135f";
    public static final int STATE_LOGIN_PHONE = 1;
    public static final int STATE_LOGIN_ACCOUNT = 2;
    public Context context;
    LoginViewControllerPresenter2 loginViewControllerPresenter2;
    public int currentState;
    public TextView titleTv, title_tip;
    public Button loginButton; //登录按钮

    public View loginPhoneButton; //跳转至手机登录界面
    public View loginAccountButton; //跳转至账号登录界面
    public View backRegisterButton; //跳转至注册


    private View loginPhoneContainer;
    private View loginAccountContainer;
    public EditText phoneEdit;
    public EditText phonePasswordEdit;
    public DrawableEditText accountEdit;
    public EditText accountPasswordEdit;

//    private LoginViewControllerPresenter2.AccountEditViewController accountEditViewController;
//    private LoginViewControllerPresenter2.HistoryPickerController historyPickerController;

    //自动填账号/手机号 缓存.
    private String autoFillCache = "";
    //data
    public String requestingPhoneNumber;
    public String requestingPhoneMd5Pwd;
    public String requestingAccount;
    public String requestingAccountMd5Pwd;

    public LoginViewController2(Context context, IDialogParam params, int login_state) {
        super(context, params);
        this.context = context;
        loginViewControllerPresenter2 = new LoginViewControllerPresenter2(this);
        initView();
        switchLoginTypeState(login_state);
        tryShowLastLoginAccount();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_login2;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    private void initView() {
        loginButton = (Button) findViewById(R.id.btn_login_container_login);
        ViewUtils.setViewEnable(loginButton, false);
        titleTv = (TextView) findViewById(R.id.tv_title_container_title);
        title_tip = (TextView) findViewById(R.id.qy_sdk_container_item_title_tip);

        loginPhoneButton = findViewById(R.id.btn_qiyuan_phone_login);
        loginAccountButton = findViewById(R.id.btn_qiyuan_account_login);
        backRegisterButton = findViewById(R.id.btn_back_to_regist);

        loginPhoneContainer = findViewById(R.id.container_item_login_phone);
        loginAccountContainer = findViewById(R.id.container_item_login_account);
        phoneEdit = (DrawableEditText) findViewById(R.id.edit_login_container_phone);
        accountEdit = (DrawableEditText) findViewById(R.id.edit_login_container_account);
//        accountEditViewController = loginViewControllerPresenter2.newAccountEditViewController(accountEdit);
//        accountEditViewController.noRightDrawableSate();
//        historyPickerController = loginViewControllerPresenter2.newHistoryPickerController(accountEdit);
        phonePasswordEdit = (EditText) findViewById(R.id.edit_login_container_phone_password);
        accountPasswordEdit = (EditText) findViewById(R.id.edit_login_container_account_password);
//        accountEdit.setOnRightDrawableClickListener(new DrawableEditText.OnDrawableClickListener() {
//            @Override
//            public void onPressed(DrawableEditText editText) {
//            }
//
//            @Override
//            public void onCancel(DrawableEditText editText) {
//
//            }
//
//            @Override
//            public void onClick(DrawableEditText editText) {
//                //try dismiss data pickper
//                int state = accountEditViewController.nextRightDrawableState();
//                Log.d(TAG, "onClick: " + state);
//                IMEUtil.hideIME(LoginViewController2.this);
//                if (state == 1) {
//                    historyPickerController.tryShowPicker();
//                } else {
//                    historyPickerController.tryDismissPicker();
//                }
//            }
//        });


//        historyPickerController.setDataPickListener(new DataPicker.DataPickerListener<LoginViewControllerPresenter2.AccountHistoryWrapper>() {
//            @Override
//            public void onDataSelected(DataPicker picker, int position) {
//                LoginViewControllerPresenter2.AccountHistoryWrapper wrapper = (LoginViewControllerPresenter2.AccountHistoryWrapper) picker.getSelectedData();
//                tryFillPasswordFromHistory(wrapper.dataName());
//                autoFillCache = wrapper.dataName();
//                accountEdit.setText(wrapper.dataName());
//            }
//
//            @Override
//            public void onDataDeleted(DataPicker picker, LoginViewControllerPresenter2.AccountHistoryWrapper data) {
//                ApiFacade.getInstance().deleteAccountHistory(String.valueOf(data.data().userID));
////                tryShowLastLoginAccount();
//
//                if (picker.getSourceData().size() < 2) {
//                    accountEdit.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            accountEditViewController.noRightDrawableSate();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onDataPickerShow(DataPicker picker) {
//                String defaultAccountOrPhone = accountEdit.getText().toString();
//                if (!StringUtils.isBlank(defaultAccountOrPhone)) {
//                    List<LoginViewControllerPresenter2.AccountHistoryWrapper> sourceData = picker.getSourceData();
//                    int position = -1;
//                    for (int i = 0; i < sourceData.size(); i++) {
//                        if (sourceData.get(i).dataName().equals(defaultAccountOrPhone)) {
//                            position = i;
//                            break;
//                        }
//                    }
//                    if (position >= 0) {
//                        picker.setSelectedPosition(position);
//                    }
//                }
//            }
//
//            @Override
//            public void onDataItemClose(final DataPicker picker, final int position, LoginViewControllerPresenter2.AccountHistoryWrapper data) {
//                int warningStringRes = R.string.warning_delete_account;
//                StandardDialog dialog
//                        = new StandardDialog(getDialogParam().getActivityContext());
//                dialog.setTitleTip(R.string.delete_account);
//                dialog.setMessageTip(ResourceHelper.getString(warningStringRes, data.dataName()));
//                dialog.setEnsureText(R.string.delete);
//                dialog.setCancelText(R.string.cancel);
//                dialog.setListener(new StandardDialog.DialogClickListener() {
//                    @Override
//                    public void onEnsureClick() {
//                        picker.deleteItem(position);
//                    }
//                });
//                dialog.show();
//            }
//
//            @Override
//            public void onDataPickerDestroy() {
//                accountEditViewController.resetRightDrawableState();
//            }
//        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IMEUtil.hideIME(LoginViewController2.this);
                if (currentState == STATE_LOGIN_PHONE) {
                    if (loginViewControllerPresenter2.checkPhoneInput(phoneEdit.getText().toString(), phonePasswordEdit.getText().toString())) {
                        loginViewControllerPresenter2.loginByPhoneImpl(requestingPhoneNumber, requestingPhoneMd5Pwd);
                    }
                } else if (currentState == STATE_LOGIN_ACCOUNT) {
                    if (loginViewControllerPresenter2.checkAccountInput(accountEdit.getText().toString(), accountPasswordEdit.getText().toString()))
                        loginViewControllerPresenter2.loginByAccountImpl(requestingAccount, requestingAccountMd5Pwd);
                }
            }
        });
        loginPhoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLoginTypeState(STATE_LOGIN_PHONE);
            }
        });
        loginAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLoginTypeState(STATE_LOGIN_ACCOUNT);
            }
        });
        backRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IMEUtil.hideIME(LoginViewController2.this);
                ViewControllerNavigator.getInstance().toRegister2(getDialogParam());
            }
        });
        ViewUtils.bindEditWithButton(phonePasswordEdit, loginButton);

        loginViewControllerPresenter2.addTextWatcher(phoneEdit, phonePasswordEdit, accountEdit, accountPasswordEdit);
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

    private void putHistoryAccount2Input(AccountHistoryInfo historyInfo) {
        if (!TextUtils.isEmpty(historyInfo.phone) && !historyInfo.phone.equals("0")) {
            autoFillCache = historyInfo.phone;
            accountEdit.setText(historyInfo.phone);
            accountEdit.setSelection(historyInfo.phone.length());
        }
    }

    private void tryShowLastLoginAccount() {
        Log.d(TAG, "tryShowLastLoginAccount: ");
        List<AccountHistoryInfo> allGameAuthHistories
                = ApiFacade.getInstance().getAccountHistories();
        if (allGameAuthHistories != null && allGameAuthHistories.size() > 1) {
            Log.d(TAG, "tryShowLastLoginAccount: size:" + allGameAuthHistories.size());
            for (AccountHistoryInfo ai : allGameAuthHistories) {
                Log.d(TAG, "tryShowLastLoginAccount: size:" + ai.toString());
            }
        }
        if (!allGameAuthHistories.isEmpty()) {
            AccountHistoryInfo historyInfo = allGameAuthHistories
                    .get(0);
            Log.d(TAG, "tryShowLastLoginAccount: historyInfo:" + historyInfo.toString());
            putHistoryAccount2Input(historyInfo);
//            tryFillPasswordFromHistory(historyInfo.username);
        } else {
            //什么都没得填
            cleanInputs();
        }
    }

    private void tryFillPasswordFromHistory(String usernameOrPhone) {
        //加点md5密码的标识
        String pwd = ApiFacade.getInstance().getPasswordFromHistoryByUsername(usernameOrPhone);
        if (StringUtils.isBlank(pwd)) {
            pwd = ApiFacade.getInstance().getPasswordFromHistoryByPhone(usernameOrPhone);
        }

        if (StringUtils.isBlank(pwd)) {
            Log.i(TAG, "fail to get pwd from history.");
            phonePasswordEdit.setText("");
        } else {
            String pwdToFill = MD5_PWD_PREFIX + pwd;
            phonePasswordEdit.setText(pwdToFill);
        }
    }


    private void switchLoginTypeState(int state) {
        if (currentState == state) {
            return;
        }
        switch (state) {
            case STATE_LOGIN_PHONE:
                title_tip.setText(R.string.str_phone_account_login);
                loginPhoneContainer.setVisibility(View.VISIBLE);
                loginAccountContainer.setVisibility(View.GONE);
                loginPhoneButton.setVisibility(View.GONE);
                loginAccountButton.setVisibility(View.VISIBLE);
                break;
            case STATE_LOGIN_ACCOUNT:
                title_tip.setText(R.string.str_qiyuan_account);
                loginPhoneContainer.setVisibility(View.GONE);
                loginAccountContainer.setVisibility(View.VISIBLE);
                loginPhoneButton.setVisibility(View.VISIBLE);
                loginAccountButton.setVisibility(View.GONE);
                break;
            default:
                return;
        }
        currentState = state;
        updateLoginButtonState();
    }

    private void updateLoginButtonState() {
        if (currentState == STATE_LOGIN_PHONE) {
            if (phoneEdit.length() == 0 || phonePasswordEdit.length() == 0
                    ) {
                loginButton.setEnabled(false);
            } else {
                loginButton.setEnabled(true);
            }
        } else if (currentState == STATE_LOGIN_ACCOUNT) {
            if (accountEdit.length() == 0 || accountPasswordEdit.length() == 0) {
                loginButton.setEnabled(false);
            } else {
                loginButton.setEnabled(true);
            }
        }
    }
}
