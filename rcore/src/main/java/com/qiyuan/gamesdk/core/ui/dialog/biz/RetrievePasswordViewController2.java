package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter.GetVerifyCodePresenter2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;

import java.util.Map;

public class RetrievePasswordViewController2 extends BaseAuthViewController {
    private static final String TAG = "QYSDK:RetrievePasswordViewController2 ";
    public Context context;
    ContainerItemTitle4 containerItemTitle4;
    public EditText phoneEdit;
    public EditText phonePasswordEdit;
    public Button getVerificationCodeButton;
    public EditText verificationCodeEdit;
    //    public ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    GetVerifyCodePresenter2 getVerifyCodePresenter2;//验证码助手类
    Button btn_enter_game;

    private String requestingPhoneNumber;
    private String requestingPhoneMd5Pwd;
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
        verificationCodeEdit = (EditText) findViewById(R.id.edit_register_container_verification_code);
        getVerifyCodePresenter2 = new GetVerifyCodePresenter2(this, phoneEdit, verificationCodeEdit, getVerificationCodeButton);
        btn_enter_game = (Button) findViewById(R.id.btn_ensure);
        ViewUtils.bindEditWithButton(phonePasswordEdit, btn_enter_game);
        ViewUtils.setViewEnable(btn_enter_game, false);
        addTextWatcher(phoneEdit, phonePasswordEdit, verificationCodeEdit);
        btn_enter_game.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtil.hideIME(RetrievePasswordViewController2.this);
                if (checkPhoneInput(phoneEdit.getText().toString(), phonePasswordEdit.getText().toString())) {
                    registerByPhoneImpl(requestingPhoneNumber, requestingPhoneMd5Pwd, verificationCodeEdit.getText().toString());
                }
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_retrieve_password2;
    }

    @Override
    public void onHide() {
        getVerifyCodePresenter2.cancelCountDown();

    }

    @Override
    public boolean onBackPressed() {
        return getVerifyCodePresenter2.onBackPressed();
    }

    public void addTextWatcher(EditText... editTexts) {
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
                    updateRegisterButtonState();
                }
            });
        }
    }

    public void updateRegisterButtonState() {
        if (phoneEdit.length() == 0 || phonePasswordEdit.length() == 0
                || verificationCodeEdit.length() == 0) {
            btn_enter_game.setEnabled(false);
        } else {
            btn_enter_game.setEnabled(true);
        }
    }

    public boolean checkPhoneInput(String phone, String password) {
        if (phone.length() != 11) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_valid_11_phone_num));
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_password_hint));
            return false;
        }
        putRegisterPhoneInfo(phone, password);
        return true;
    }

    public void putRegisterPhoneInfo(String phone, String md5Pwd) {
        requestingPhoneNumber = phone;
        requestingPhoneMd5Pwd = md5Pwd;
    }

    public void registerByPhoneImpl(final String phone, final String password, final String vCode) {
        showLoading();
        QyRespListener<AuthModel> callback = new QyRespListener<AuthModel>() {
            @Override
            public void onNetSucc(String url, Map params, AuthModel result) {
                if (params != null) {
                    Log.d(TAG, "registerByPhone " + result.toString());
                    notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
                    close();
                } else {
                    Log.d(TAG, "registerByPhone return null;");
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
        };

        ApiFacade.getInstance().registerByPhone(phone, password,
                vCode, callback);

    }
}
