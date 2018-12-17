package com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.ui.dialog.biz.RegisterViewController2;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewControllerPresenter2 {
    RegisterViewController2 registerViewController2;

    public RegisterViewControllerPresenter2(RegisterViewController2 controller) {
        registerViewController2 = controller;
    }
    public void updateRegisterButtonState() {
        if (registerViewController2.currentState == registerViewController2.STATE_REGISTER_PHONE) {
            if (registerViewController2.phoneEdit.length() == 0 || registerViewController2.phonePasswordEdit.length() == 0
                    || registerViewController2.verificationCodeEdit.length() == 0) {
                registerViewController2.registerButton.setEnabled(false);
            } else {
                registerViewController2.registerButton.setEnabled(true);
            }
        } else if (registerViewController2.currentState == registerViewController2.STATE_REGISTER_ACCOUNT) {
            if (registerViewController2.accountPasswordEdit.length() == 0) {
                registerViewController2.registerButton.setEnabled(false);
            } else {
                registerViewController2.registerButton.setEnabled(true);
            }
        }
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

    public boolean checkPhoneInput(String phone, String password) {
        if (phone.length() != 11) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_valid_11_phone_num));
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_password_hint));
            return false;
        }
        registerViewController2.putRegisterPhoneInfo(phone, password);
        return true;
    }

    public boolean checkAccountInput(String password) {
//        if (account.length() < 4 || account.length() > 16) {
//            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_account_hint));
//            return false;
//        }
        if (password.length() < 6 || password.length() > 16) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_password_hint));
            return false;
        }
        String account = "aaaaaaaaa";
        registerViewController2.putRegisterAccountInfo(account, password);
        return true;
    }

    private void tipTologin() {
        ToastUtils.showMsg("提示前往登录页面吧");
    }

    /**
     * 获取短信验证码
     *
     * @param phone 手机号码
     */
    public void getVerificationCodeButtonImpl(String phone) {
        registerViewController2.showLoading();
        ApiFacade.getInstance().requestVerificationCode(phone, IAuthApi.VCODE_TYPE_REGISTER, registerViewController2.retryTime, new QyRespListener<Void>() {
            @Override
            public void onNetSucc(String url, Map<String, String> params, Void result) {
                registerViewController2.hideLoading();
                registerViewController2.retryTime++;
                if (params != null) {
                    registerViewController2.waitingVerifyCode = true;
                    Log.d(registerViewController2.TAG, "success request verify code ");
                    ToastUtils.showMsg(R.string.already_sent_verification_tips);
                    registerViewController2.reGetVerifyCodeButtonController.prepare();
                    registerViewController2.reGetVerifyCodeButtonController.startCountDown();
                } else {
                    Log.d(registerViewController2.TAG, "error request verify code.");
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                registerViewController2.hideLoading();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                registerViewController2.hideLoading();
            }
        });
    }

    public void registerByPhoneImpl(final String phone, final String password, final String vCode) {
        registerViewController2.showLoading();
        QyRespListener<AuthModel> callback = new QyRespListener<AuthModel>() {
            @Override
            public void onNetSucc(String url, Map params, AuthModel result) {
                if (params != null) {
                    Log.d(registerViewController2.TAG, "registerByPhone " + result.toString());
                    registerViewController2.notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
                    registerViewController2.close();
                } else {
                    Log.d(registerViewController2.TAG, "registerByPhone return null;");
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                registerViewController2.hideLoading();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                registerViewController2.hideLoading();
            }
        };

        ApiFacade.getInstance().registerByPhone(phone, password,
                vCode, callback);

    }

    public void invokeRegisterByPhone(String phone, String password, String vCode) {
        registerViewController2.showLoading();
        QyRespListener<AuthModel> callback = new QyRespListener<AuthModel>() {
            @Override
            public void onNetSucc(String url, Map params, AuthModel result) {
                if (params != null) {
                    Log.d(registerViewController2.TAG, "registerByPhone " + result.toString());
                    registerViewController2.notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
                    registerViewController2.close();
                } else {
                    Log.d(registerViewController2.TAG, "registerByPhone return null;");
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                registerViewController2.hideLoading();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                registerViewController2.hideLoading();
            }
        };

        ApiFacade.getInstance().registerByPhone(phone, password,
                vCode, callback);
    }

    public void registerByAccountImpl(final String account, final String password) {
        registerViewController2.showLoading();
        ApiFacade.getInstance().registerByUserName(password, account, new QyRespListener<AuthModel>() {
            @Override
            public void onNetworkComplete() {
                super.onNetworkComplete();
                registerViewController2.hideLoading();
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, AuthModel result) {
                if (params != null) {
                    Log.d(registerViewController2.TAG, "registerByPhone " + result.toString());
                    registerViewController2.notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
                    registerViewController2.close();
                } else {
                    Log.d(registerViewController2.TAG, "registerByPhone return null;");
                }
            }
        });
    }


    public SmsVerificationCode newHistoryPickerController(Handler handler) {
        return new SmsVerificationCode(handler);
    }
    /*
     * 监听短信数据库
     */
    public class SmsVerificationCode extends ContentObserver {

        private Uri SMS_ALL = Uri.parse("content://sms/");
        private Cursor cursor = null;

        public SmsVerificationCode(Handler handler) {
            super(handler);
        }


        public void getSmsFromPhone() {
            ContentResolver cr = registerViewController2.mContext.getContentResolver();
            String[] projection = new String[]{"body"};
            // 读取短信内容
            cursor = cr.query(SMS_ALL, projection, null, null, null);
            if (null == cursor) {
                return;
            }
            if (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body")); //获取短信的内容
                //根据正则分组表达式获取验证码  // TODO: 2017/8/10  改成起源的
                Pattern pattern = Pattern.compile("(【QY语音】|【趣丸网】)(\\s*验证码\\s*)([0-9]{4})");
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    registerViewController2.verificationCodeEdit.setText(matcher.group(3));
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSmsFromPhone();
        }
    }
}
