package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qygame.qysdk.outer.util.StringUtils;

import java.util.Map;

public class RetrievePasswordViewController2 extends BaseAuthViewController {
    private static final String TAG = "QYSDK:RetrievePasswordViewController2 ";
    public Context context;
    ContainerItemTitle4 containerItemTitle4;
    public EditText phoneEdit;
    public EditText phonePasswordEdit;
    public Button getVerificationCodeButton;
    public EditText verificationCodeEdit;
    private int retryTime = 0;
    private boolean waitingVerifyCode = false;
    public ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    Button btn_enter_game;

//    ContainerItemBottom2 containerItemBottom2;

    public RetrievePasswordViewController2(Context context, IDialogParam params) {
        super(context, params);
        this.context = context;
        initView();
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_qiyuan_sdk_game_title);
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

        getVerificationCodeButton = (Button) findViewById(R.id.btn_register_container_get_verification_code);
        ViewUtils.setViewEnable(getVerificationCodeButton, false);
        phoneEdit = (EditText) findViewById(R.id.edit_register_container_phone);
        phonePasswordEdit = (EditText) findViewById(R.id.edit_register_container_password);
        reGetVerifyCodeButtonController = new ReGetVerifyCodeButtonController(getVerificationCodeButton);
        verificationCodeEdit = (EditText) findViewById(R.id.edit_register_container_verification_code);
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
                IMEUtil.hideIME(RetrievePasswordViewController2.this);
                //获取验证码
                getVerificationCodeButtonImpl(phone);
            }
        });

        btn_enter_game = (Button) findViewById(R.id.btn_has_registered_container_enter_game);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_retrieve_password2;
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
