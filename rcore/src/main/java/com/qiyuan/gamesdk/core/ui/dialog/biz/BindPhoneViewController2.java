package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.bean.BindPhoneBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qygame.qysdk.outer.util.StringUtils;

import java.util.Map;

public class BindPhoneViewController2 extends BaseAuthViewController {
    private static final String TAG = "QYSDK:BindPhoneViewController2 ";
    Context mContext;
    ContainerItemTitle4 containerItemTitle4;
    //    private TextView titleTv;
//    private TextView closeTitleContainerBtn;
    private EditText accountEdit; //输入号码框
    private EditText verificationCodeEdit; //验证码
    private Button getVerificationCodeButton; //获取验证码
    private Button bindButton; //绑定按钮
    ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    private int retryTime = 0;
    private boolean waitingVerifyCode = false;

    public BindPhoneViewController2(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_bind_phone2;
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_bind_phone_title2);
        containerItemTitle4.setTitleOnclick(new ContainerItemTitle4.TitleOnclick() {
            @Override
            public void toBack() {
                ViewControllerNavigator.getInstance().toAccountCenter2();
            }

            @Override
            public void toRefresh() {

            }

            @Override
            public void toClose() {
                close();
            }
        });
        accountEdit = (EditText) findViewById(R.id.edit_bind_phone_container_account);
        verificationCodeEdit = (EditText) findViewById(R.id.edit_bind_phone_container_verification_code);
        getVerificationCodeButton = (Button) findViewById(R.id.btn_bind_phone_container_get_verification_code);
        ViewUtils.setViewEnable(getVerificationCodeButton, false);
        reGetVerifyCodeButtonController = new ReGetVerifyCodeButtonController(getVerificationCodeButton);
        bindButton = (Button) findViewById(R.id.btn_bind_phone_container_bind);
        ViewUtils.setViewEnable(bindButton, false);

        addTextWatcher(accountEdit, verificationCodeEdit);

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
                IMEUtil.hideIME(BindPhoneViewController2.this);
                //获取验证码
                getVerificationCodeButtonImpl(phone);
            }
        });
        bindButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtil.hideIME(BindPhoneViewController2.this);
//                bindPhone();
                ViewControllerNavigator.getInstance().toBindPhoneSuccessful2();
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
                    updateButtonState();
                }
            });
        }
    }

    private void updateButtonState() {
        ViewUtils.setViewEnable(getVerificationCodeButton, accountEdit.length() == 11 && reGetVerifyCodeButtonController.getmMillisUntilFinished() == 0);
        ViewUtils.setViewEnable(bindButton, accountEdit.length() == 11 && verificationCodeEdit.length() == 4);
//        if (accountEdit.length() == 0 || verificationCodeEdit.length() == 0) {
//            ViewUtils.setViewEnable(bindButton, false);
//        } else {
//            ViewUtils.setViewEnable(bindButton, true);
//        }
    }

    private void bindPhone() {
        final String phone = accountEdit.getText().toString();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(verificationCodeEdit.getText().toString())) {
            ToastUtils.showMsg("请不要输入空字符");
            return;
        }
        ApiFacade.getInstance().bindPhone2(accountEdit.getText().toString(), verificationCodeEdit.getText().toString(), new QyRespListener<BindPhoneBean>() {
            @Override
            public void onNetSucc(String url, Map params, BindPhoneBean result) {
                super.onNetSucc(url, params, result);
                if (result.getCode() == 1) {
                    AccountHistoryInfo accountHistoryInfo = ApiFacade.getInstance().getCurrentHistoryAccount();
                    accountHistoryInfo.phone = phone;
                    ApiFacade.getInstance().insertOrUpdateAccountHistory(accountHistoryInfo);
                    ToastUtils.showMsg(R.string.bind_phone_succ + result.getMsg());
                    close();
//                clearRequestInput();
//                EventDispatcherAgent.defaultAgent().broadcast(FinishFragmentEvent.TYPE_FINISH_FRAGMENT,
//                        new FinishFragmentEvent.Param(0, getActivity(), getParentFragment()));
                } else {
                    super.onFail(result.getCode(), R.string.bind_phone_succ + result.getMsg());
                }

            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
            }

            @Override
            public void onNetError(String url, Map params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
            }

            @Override
            public void onNetworkComplete() {
                super.onNetworkComplete();
//                loadingDialog.dismiss();
            }
        });
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
}
