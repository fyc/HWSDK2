package com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.ui.dialog.biz.BaseAuthViewController;
import com.qiyuan.gamesdk.core.ui.dialog.biz.ReGetVerifyCodeButtonController;
import com.qiyuan.gamesdk.core.ui.widget.StandardDialog;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qygame.qysdk.outer.util.StringUtils;

import java.util.Map;

public class GetVerifyCodePresenter2 {
    private static final String TAG = "QYSDK:GetVerifyCodePresenter2 ";
    private int retryTime = 0;
    private boolean waitingVerifyCode = false;
    public EditText phoneEdit;
    EditText verificationCodeEdit;
    public Button getVerificationCodeButton;
    public ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    BaseAuthViewController baseAuthViewController;

    public GetVerifyCodePresenter2(final BaseAuthViewController baseAuthViewController, final EditText phoneEdit, EditText verificationCodeEdit, final Button getVerificationCodeButton) {
        this.baseAuthViewController = baseAuthViewController;
        this.phoneEdit = phoneEdit;
        this.getVerificationCodeButton = getVerificationCodeButton;
        reGetVerifyCodeButtonController = new ReGetVerifyCodeButtonController(getVerificationCodeButton);
        getVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneEdit.getText().toString();
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
                IMEUtil.hideIME(baseAuthViewController);
                //获取验证码
                getVerificationCodeButtonImpl(phone);
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
    }

    /**
     * 获取短信验证码
     *
     * @param phone 手机号码
     */
    private void getVerificationCodeButtonImpl(String phone) {
        baseAuthViewController.showLoading();
        ApiFacade.getInstance().requestVerificationCode2(phone, IAuthApi.VCODE_TYPE_REGISTER, retryTime, new QyRespListener<String>() {
            @Override
            public void onNetSucc(String url, Map<String, String> params, String result) {
                baseAuthViewController.hideLoading();
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
                baseAuthViewController.hideLoading();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                baseAuthViewController.hideLoading();
            }
        });
    }

    public void cancelCountDown() {
        reGetVerifyCodeButtonController.cancelCountDown();
    }

    public boolean onBackPressed() {
        boolean blockBackAction = waitingVerifyCode;
        if (waitingVerifyCode) {
            IMEUtil.hideIME(baseAuthViewController);
            warningExitRegister(new TwoChoiceJob() {
                @Override
                public void jobOnChoose() {
                    waitingVerifyCode = false;
                    baseAuthViewController.close();
                }

                @Override
                public void jobDefault() {

                }
            });
        }
        return blockBackAction;
    }

    private void warningLeaveRegister(final TwoChoiceJob job) {
        if (waitingVerifyCode) {
            StandardDialog dialog
                    = new StandardDialog(baseAuthViewController.getDialogParam().getActivityContext());
            dialog.setMessageTip(R.string.warning_back_to_register);
            dialog.setEnsureText(R.string.restart);
            dialog.setCancelText(R.string.keep_waiting);
            dialog.setListener(new StandardDialog.DialogClickListener() {
                @Override
                public void onEnsureClick() {
//                    verificationCodeEdit.setText("");
                    cancelCountDown();
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
                    = new StandardDialog(baseAuthViewController.getDialogParam().getActivityContext());
            dialog.setMessageTip(R.string.warning_back_to_register);
            dialog.setEnsureText(R.string.yes_i_wanna_exit);
            dialog.setCancelText(R.string.keep_waiting);
            dialog.setListener(new StandardDialog.DialogClickListener() {
                @Override
                public void onEnsureClick() {
//                    verificationCodeEdit.setText("");
//                    reGetVerifyCodeButtonController.cancelCountDown();
                    cancelCountDown();
                    job.jobOnChoose();
                }
            });
            dialog.show();
        } else {
            job.jobDefault();
        }
    }

    private interface TwoChoiceJob {
        void jobOnChoose();

        void jobDefault();
    }
}
