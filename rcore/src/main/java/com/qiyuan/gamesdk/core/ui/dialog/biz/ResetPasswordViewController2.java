package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.TextInputBox;
import com.qiyuan.gamesdk.util.IMEUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.IDialogParam;

/**
 * Created by Nekomimi on 2017/4/25.
 */

public class ResetPasswordViewController2 extends BaseAuthViewController {
    private static final String TAG = "QYSDK:ResetPasswordViewContro2";

    ContainerItemTitle4 containerItemTitle4;
    //    private EditText phonePasswordEdit;
    TextInputBox edit_old_password;
    TextInputBox edit_new_password;
    TextInputBox edit_again_new_password;
    private Button confirmButton;


    //data
    private String oldPassword;
    private String newPassword;
    Context mContext;

    private int retryTime = 0;

    public ResetPasswordViewController2(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.account_reset_passwd);
        containerItemTitle4.setTitleBtnVisibility(true,false,true);
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
        edit_old_password = (TextInputBox) findViewById(R.id.edit_old_password);
        edit_new_password = (TextInputBox) findViewById(R.id.edit_new_password);
        edit_again_new_password = (TextInputBox) findViewById(R.id.edit_again_new_password);
        confirmButton = (Button) findViewById(R.id.btn_reset_pwd_confirm);
        ViewUtils.setViewEnable(confirmButton, false);

        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtil.hideIME(ResetPasswordViewController2.this);
//                if (checkInput()) {
////                    resetPassword(edit_old_password.getText().toString(), edit_new_password.getText().toString());
////                }
                ViewControllerNavigator.getInstance().toResetPasswordSuccessful2();
            }
        });
        addTextWatcher(edit_old_password.getEdt_input(), edit_new_password.getEdt_input(), edit_again_new_password.getEdt_input());
    }

    private boolean checkInput() {
        int msg = 0;
        if (edit_new_password.length() < 6 || edit_new_password.length() > 16) {
            msg = R.string.reset_password_new_password_tip;
        }
        if (msg != 0) {
            ToastUtils.showMsg(msg);
            return false;
        } else {
            return true;
        }
    }

    private void putRequestInput(String old_pwd, String new_pwd) {
        oldPassword = old_pwd;
        newPassword = new_pwd;
    }

    private void clearRequestInput() {
        oldPassword = "";
        newPassword = "";
    }
    public void resetPassword(String old_pwd, String new_pwd){
        putRequestInput(old_pwd, new_pwd);
        showLoading();
//        ApiFacade.getInstance().forgetPassword(requestingPhoneNumber, requestingPhonePwd,vCode,new QyRespListener(){
//            @Override
//            public void onNetSucc(String url, Map params, Object result) {
//                super.onNetSucc(url, params, result);
//                ToastUtils.showMsg(R.string.resetpasswd_succ);
//                AccountHistoryInfo info = ApiFacade.getInstance().getHistoryAccountByPhone(requestingPhoneNumber);
////                info.password = (String)params.get("newpwd");
//                ApiFacade.getInstance().insertOrUpdateAccountHistory(info);
//                clearRequestInput();
//                ViewControllerNavigator.getInstance().toLogin( getDialogParam());
//            }
//
//            @Override
//            public void onNetError(String url, Map params, String errno, String errmsg) {
//                clearRequestInput();
//                ToastUtils.showMsg(errmsg);
//            }
//
//            @Override
//            public void onFail(int errorNo, String errmsg) {
//                Log.e(TAG,"errorNo: " +errorNo+" errmsg: "+errmsg);
//                clearRequestInput();
//            }
//
//            @Override
//            public void onNetworkComplete() {
//                super.onNetworkComplete();
//                hideLoading();
//            }
//        });

    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_reset_pwd2;
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
    }

    private void updateConfirmButtonState() {
        if (edit_old_password.length() == 0 || edit_new_password.length() == 0
                || edit_again_new_password.length() == 0) {
            confirmButton.setEnabled(false);
        } else {
            confirmButton.setEnabled(true);
        }

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
                    updateConfirmButtonState();
                }
            });
        }
    }

}