package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qygame.qysdk.outer.util.StringUtils;
import com.qiyuan.gamesdk.PluginManager;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.widget.DataPicker;
import com.qiyuan.gamesdk.core.ui.widget.DrawableEditText;
import com.qiyuan.gamesdk.core.ui.widget.StandardDialog;
import com.qiyuan.gamesdk.core.ui.widget.TipsAlertDialogView;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.PopUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Orange on 15/6/10.
 */
public class LoginViewController extends BaseAuthViewController {

    private static final String TAG = "QYSDK:LoginViewController ";
    private static final String MD5_PWD_PREFIX = "\u0d62\u0d63\u135f";
    Context context;
    private InputFilter[] phoneLengthFilter = new InputFilter[]{
            new InputFilter.LengthFilter(11)
    };


    private Button loginButton; //登录按钮
    private View backRegisterButton; //跳转至注册
    View btn_terms_of_sevrvice;
    //    private View resetPasswordBtn;
    private EditText verificationCodeEdit; //验证码
    private Button getVerificationCodeButton; //获取验证码
    private View titleImg;
    private TextView titleTv;
    private DrawableEditText accountEdit; //输入号码框
    //    private EditText passwordEdit;
    private TextView backTitleContainerBtn;
    private TextView closeTitleContainerBtn;

    TextView btn_login_container_visitors_to_login;//游客登录

    private AccountEditViewController accountEditViewController;
    private HistoryPickerController historyPickerController;

    private int retryTime = 0;
    private boolean waitingVerifyCode = false;
    ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    //自动填账号/手机号 缓存.
    private String autoFillCache = "";


    public LoginViewController(Context context, IDialogParam params) {
        super(context, params);
        this.context = context;
        initView();
        tryShowLastLoginAccount();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_login;
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
        titleImg = findViewById(R.id.img_title_container_title);
//        resetPasswordBtn = findViewById(R.id.btn_login_container_forget_password);
        backRegisterButton = findViewById(R.id.btn_login_container_back_register);
        btn_terms_of_sevrvice = findViewById(R.id.btn_login_container_register_the_terms_of_service);
        accountEdit = (DrawableEditText) findViewById(R.id.edit_login_container_account);
        accountEditViewController = new AccountEditViewController(accountEdit);
        accountEditViewController.noRightDrawableSate();
        historyPickerController = new HistoryPickerController(accountEdit);
//        passwordEdit = (EditText) findViewById(R.id.edit_login_container_password);
        verificationCodeEdit = (EditText) findViewById(R.id.edit_login_container_verification_code);
        getVerificationCodeButton = (Button) findViewById(R.id.btn_login_container_get_verification_code);
        ViewUtils.setViewEnable(getVerificationCodeButton, false);
        reGetVerifyCodeButtonController = new ReGetVerifyCodeButtonController(getVerificationCodeButton);
        btn_login_container_visitors_to_login = (TextView) findViewById(R.id.btn_login_container_visitors_to_login);

        backTitleContainerBtn = (TextView) findViewById(R.id.btn_title_container_back);
        closeTitleContainerBtn = (TextView) findViewById(R.id.btn_title_container_close);
        backTitleContainerBtn.setVisibility(GONE);
        titleImg.setVisibility(VISIBLE);
        titleTv.setVisibility(GONE);
        closeTitleContainerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        accountEdit.addTextChangedListener(new TextWatcher() {
            boolean blankHit = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = accountEdit.getText().toString();
                if (!blankHit) {
                    if (StringUtils.isBlank(str)) {
                        blankHit = true;
                    }
                } else {
                    blankHit = false;
                }
                ViewUtils.setViewEnable(loginButton, accountEdit.length() != 0);
                ViewUtils.setViewEnable(getVerificationCodeButton, accountEdit.length() != 0);
            }
        });

        accountEdit.setOnRightDrawableClickListener(new DrawableEditText.OnDrawableClickListener() {
            @Override
            public void onPressed(DrawableEditText editText) {
            }

            @Override
            public void onCancel(DrawableEditText editText) {

            }

            @Override
            public void onClick(DrawableEditText editText) {
                //try dismiss data pickper
                int state = accountEditViewController.nextRightDrawableState();
                Log.d(TAG, "onClick: " + state);
                IMEUtil.hideIME(LoginViewController.this);
                if (state == 1) {
                    historyPickerController.tryShowPicker();
                } else {
                    historyPickerController.tryDismissPicker();
                }
            }
        });


        historyPickerController.setDataPickListener(new DataPicker.DataPickerListener<AccountHistoryWrapper>() {
            @Override
            public void onDataSelected(DataPicker picker, int position) {
                AccountHistoryWrapper wrapper = (AccountHistoryWrapper) picker.getSelectedData();
                tryFillPasswordFromHistory(wrapper.dataName());
                autoFillCache = wrapper.dataName();
                accountEdit.setText(wrapper.dataName());
            }

            @Override
            public void onDataDeleted(DataPicker picker, AccountHistoryWrapper data) {
                ApiFacade.getInstance().deleteAccountHistory(String.valueOf(data.data().userID));
//                tryShowLastLoginAccount();

                if (picker.getSourceData().size() < 2) {
                    accountEdit.post(new Runnable() {
                        @Override
                        public void run() {
                            accountEditViewController.noRightDrawableSate();
                        }
                    });
                }
            }

            @Override
            public void onDataPickerShow(DataPicker picker) {
                String defaultAccountOrPhone = accountEdit.getText().toString();
                if (!StringUtils.isBlank(defaultAccountOrPhone)) {
                    List<AccountHistoryWrapper> sourceData = picker.getSourceData();
                    int position = -1;
                    for (int i = 0; i < sourceData.size(); i++) {
                        if (sourceData.get(i).dataName().equals(defaultAccountOrPhone)) {
                            position = i;
                            break;
                        }
                    }
                    if (position >= 0) {
                        picker.setSelectedPosition(position);
                    }
                }
            }

            @Override
            public void onDataItemClose(final DataPicker picker, final int position, AccountHistoryWrapper data) {
                int warningStringRes = R.string.warning_delete_account;
                StandardDialog dialog
                        = new StandardDialog(getDialogParam().getActivityContext());
                dialog.setTitleTip(R.string.delete_account);
                dialog.setMessageTip(ResourceHelper.getString(warningStringRes, data.dataName()));
                dialog.setEnsureText(R.string.delete);
                dialog.setCancelText(R.string.cancel);
                dialog.setListener(new StandardDialog.DialogClickListener() {
                    @Override
                    public void onEnsureClick() {
                        picker.deleteItem(position);
                    }
                });
                dialog.show();
            }

            @Override
            public void onDataPickerDestroy() {
                accountEditViewController.resetRightDrawableState();
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IMEUtil.hideIME(LoginViewController.this);
                loginImpl(accountEdit.getText().toString(), verificationCodeEdit.getText().toString());
            }
        });
        backRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                IMEUtil.hideIME(LoginViewController.this);
//                ViewControllerNavigator.getInstance().toRegister(getDialogParam());
            }
        });
        btn_terms_of_sevrvice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtil.hideIME(LoginViewController.this);
                ViewControllerNavigator.getInstance().toTermsOfServiceAlertDialogView(context, getDialogParam());
            }
        });
//        resetPasswordBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IMEUtil.hideIME(LoginViewController.this);
//                ViewControllerNavigator.getInstance().toResetPassword(getDialogParam());
//            }
//        });
        btn_login_container_visitors_to_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                IMEUtil.hideIME(LoginViewController.this);
//                ViewControllerNavigator.getInstance().toBindPhone(getDialogParam());

                loginVisitorsWrapImpl();
            }
        });
//        ViewUtils.bindEditWithButton(passwordEdit, loginButton);

//        addTextWatcher(accountEdit, passwordEdit);

        getVerificationCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = accountEdit.getText().toString();
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
                IMEUtil.hideIME(LoginViewController.this);
                //获取验证码
                getVerificationCodeButtonImpl(phone);
            }
        });
    }

    private void addTextWatcher(EditText... editTexts) {
        for (final EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    updateLoginButtonState();
                }
            });
        }
    }


    private void updateLoginButtonState() {
        if (accountEdit.length() == 0 || verificationCodeEdit.length() == 0) {
            ViewUtils.setViewEnable(loginButton, false);
        } else {
            ViewUtils.setViewEnable(loginButton, true);
        }
    }

    /**
     * 获取短信验证码
     *
     * @param phone 手机号码
     */
    private void getVerificationCodeButtonImpl(String phone) {
        showLoading();
        ApiFacade.getInstance().requestVerificationCode2(phone, IAuthApi.VCODE_TYPE_REGISTER, retryTime, new QyRespListener<String>() {
            @Override
            public void onNetSucc(String url, Map<String, String> params, String result) {
                hideLoading();
                retryTime++;
                if (params != null) {
                    waitingVerifyCode = true;
                    Log.d(TAG, "success request verify code ");
                    Log.d(TAG, "success request result " + result);
                    ToastUtils.showMsg(R.string.already_sent_verification_tips);
                    reGetVerifyCodeButtonController.prepare();
                    reGetVerifyCodeButtonController.startCountDown();
                } else {
                    Log.d(TAG, "error request verify code.");
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                hideLoading();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                hideLoading();
            }
        });
    }

    private void loginImpl(String account, String code) {
        if (StringUtils.isBlank(account)) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.phone_blank));
            return;
        }
        if (account.length() != 11) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_11_phone_number));
            return;
        }
        if (!account.startsWith("1") && !account.startsWith("9")) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_valid_number));
            return;
        }
        loginButton.setEnabled(false);
        showLoading();
        ApiFacade.getInstance().login2(account, code, new QyRespListener<LoginBean>() {
            @Override
            public void onNetworkComplete() {
                loginButton.setEnabled(true);
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, LoginBean result) {
//                onLoginSuccess(result);
                hideLoading();
                notifyAuthResult2(StatusCodeDef.SUCCESS, "", result);
                PopUtil.get((Activity) context).showNoButton(result.getData().getMobile_phone() + "，");
                close();
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                onLoginFail();
                loginButton.setEnabled(true);
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                onLoginFail();
                loginButton.setEnabled(true);
            }
        });
    }


    public void loginVisitorsWrapImpl() {
        List<AccountHistoryInfo> allGameAuthHistories
                = ApiFacade.getInstance().getAccountHistories();
        if (!allGameAuthHistories.isEmpty()) {
            AccountHistoryInfo historyInfo = allGameAuthHistories
                    .get(0);
            if (!TextUtils.isEmpty(historyInfo.guest) && historyInfo.guest.equals("false")) {
                loginVisitorsImpl();//进入游客登录流程
            } else {
                loginAutoImpl();//进入自动登录流程
            }
        } else {
            loginVisitorsImpl();//进入游客登录流程
        }
    }

    public void loginVisitorsImpl() {
        showLoading();
        ApiFacade.getInstance().loginVisitors(new QyRespListener<LoginBean>() {
            @Override
            public void onNetworkComplete() {
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, LoginBean result) {
                hideLoading();
                notifyAuthResult2(StatusCodeDef.SUCCESS, "", result);
                PopUtil.get((Activity) context).showNoButton(result.getData().getUser_id() + "，");
                close();
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                onLoginFail();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                onLoginFail();
            }
        });
    }

    public void loginAutoImpl() {
        showLoading();
        ApiFacade.getInstance().loginAuto(new QyRespListener<LoginBean>() {
            @Override
            public void onNetworkComplete() {
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, final LoginBean result) {
                hideLoading();
                notifyAuthResult2(StatusCodeDef.SUCCESS, "", result);
                if (!TextUtils.isEmpty(result.getData().getGuest()) && result.getData().getGuest().equals("true")) { //游客，进入手机绑定
                    final PopUtil popUtil = PopUtil.get((Activity) context);
                    popUtil.showHasButton("游客：" + result.getData().getUser_id() + "，",
                            new PopUtil.PopOnListener() {
                                @Override
                                public void onClick(View v) {
                                    ApiFacade.getInstance().logout(PluginManager.getInstance().getLogoutCallback());
                                    popUtil.dismiss();
                                }

                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    ViewControllerNavigator.getInstance().toTipsAlertDialogView(context)
                                            .setOnclick(new TipsAlertDialogView.Onclick() {
                                                @Override
                                                public void onPositive() {
                                                    ViewControllerNavigator.getInstance().toBindPhone(getDialogParam());
                                                }

                                                @Override
                                                public void onNegative() {
                                                }
                                            });
                                }
                            });
                } else if (!TextUtils.isEmpty(result.getData().getGuest()) && result.getData().getGuest().equals("false")) {//手机用户，进入实名认证
                    final PopUtil popUtil = PopUtil.get((Activity) context);
                    popUtil.showHasButton(result.getData().getMobile_phone() + "，",
                            new PopUtil.PopOnListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "切换账号，假装成功退出=1=" + ApiFacade.getInstance().getMainUid());
                                    ApiFacade.getInstance().logout(PluginManager.getInstance().getLogoutCallback());
                                    popUtil.dismiss();
                                }

                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    if (result.getData().getNeed_real().equals("1")) {
                                        ViewControllerNavigator.getInstance().toRealNameAuth(getDialogParam());
                                    }
                                }
                            });
                }
                close();
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                onLoginFail();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                onLoginFail();
            }
        });
    }


    private void onLoginFail() {
        hideLoading();
    }

    private void onLoginSuccess(AuthModel result) {
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
//            passwordEdit.setText("");
        } else {
            String pwdToFill = MD5_PWD_PREFIX + pwd;
//            passwordEdit.setText(pwdToFill);
        }
    }

    class AccountEditViewController {
        DrawableEditText editView;
        int rightDrawableStateCount = 0;
        boolean noRightDrawable;

        public AccountEditViewController(DrawableEditText accountEditView) {
            editView = accountEditView;
        }

        public void noRightDrawableSate() {
            noRightDrawable = true;
            rightDrawableStateCount = 0;
            editView.setDrawableRight(0);
        }

        public void resetRightDrawableState() {
            noRightDrawable = false;
            rightDrawableStateCount = 0;
            editView.setDrawableRight(R.drawable.qy_sdk_icon_expand);
        }

        public int nextRightDrawableState() {
            int state = -1;
            if (noRightDrawable) {
                return state;
            }
            state = ++rightDrawableStateCount % 2;
            if (state == 0) {
                editView.setDrawableRight(R.drawable.qy_sdk_icon_expand);
            } else {
                editView.setDrawableRight(R.drawable.qy_sdk_icon_collapse);
            }
            return state;
        }
    }

    class HistoryPickerController {

        DataPicker picker = null;
        View anchor;
        DataPicker.DataPickerListener<AccountHistoryWrapper> listener = null;

        public HistoryPickerController(View anchorView) {
            anchor = anchorView;
        }

        public void tryDismissPicker() {
            if (picker != null) {
                picker.dismiss();
                picker.setDataPickerListener(null);
                picker = null;
            }
        }

        public void tryShowPicker() {
            tryDismissPicker();
            tryListAccountHistories();
        }

        private void tryListAccountHistories() {

            List<AccountHistoryInfo> allGameAuthHistories
                    = ApiFacade.getInstance().getAccountHistories();
            Log.d(TAG, "tryListAccountHistories: " + allGameAuthHistories.size());
            if (allGameAuthHistories.isEmpty()) {
                return;
            }
            showNewDataPicker(toAccountHistoryWrapper(allGameAuthHistories));
        }


        private void showNewDataPicker(List<AccountHistoryWrapper> data) {
            int height = (int) (ViewUtils.distanceToScreenBottom(anchor) * 0.6f);
            int width = (int) (anchor.getWidth());
//            int dx = (int) ((anchor.getWidth() - width) / 2.0f);
            picker = new DataPicker<AccountHistoryWrapper>(getContext(), width, height, true);
            picker.setDataSource(data);
            picker.setDataPickerListener(listener);
            picker.showAsDropDown(LoginViewController.this.accountEdit, 0, 0);
        }

        private List<AccountHistoryWrapper> toAccountHistoryWrapper(
                @NonNull List<AccountHistoryInfo> authHistories) {
            List<AccountHistoryWrapper> wrapperList
                    = new ArrayList<>(authHistories.size());
            for (AccountHistoryInfo info : authHistories) {
                wrapperList.add(new AccountHistoryWrapper(info));
            }
            return wrapperList;
        }

        public void setDataPickListener(DataPicker.DataPickerListener<AccountHistoryWrapper> l) {
            listener = l;
        }
    }

    private static class AccountHistoryWrapper implements DataPicker.IDataWrapper {
//        public static final int DATA_TYPE_ACCOUNT = 0;
//        public static final int DATA_TYPE_PHONE = 1;

        private AccountHistoryInfo orgInfo;
//        private int dataType = DATA_TYPE_ACCOUNT;

        public AccountHistoryWrapper(@NonNull AccountHistoryInfo orgInfo) {
            this.orgInfo = orgInfo;
//            this.dataType = dataType;
        }

        @Override
        public String dataName() {

            return orgInfo.phone;
        }

        @Override
        public String key() {
            return String.valueOf(orgInfo.userID);
        }

        public AccountHistoryInfo data() {
            return orgInfo;
        }

    }

}
