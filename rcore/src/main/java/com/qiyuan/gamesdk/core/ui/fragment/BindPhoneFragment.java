package com.qiyuan.gamesdk.core.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.ui.common.CommonTitlePrimaryFragment;
import com.qiyuan.gamesdk.core.ui.dialog.biz.LoadingDialog;
import com.qiyuan.gamesdk.core.update.DefaultUpdateImpl;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.event.EventDispatcherAgent;
import com.qygame.qysdk.outer.event.FinishFragmentEvent;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qygame.qysdk.outer.util.StringUtils;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.ui.common.CommonTitlePrimaryFragment;
import com.qiyuan.gamesdk.core.ui.dialog.biz.LoadingDialog;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static com.qiyuan.gamesdk.core.update.DefaultUpdateImpl.TAG;

/**
 * Created by Nekomimi on 2017/4/28.
 */

public class BindPhoneFragment extends BaseFragment {

    private EditText phoneEdt;
    private EditText vCodeEdt;
    private Button vCodeGetBtn;
    private Button confirmBtn;
    private LoadingDialog loadingDialog;

    private int retryTime = 0;

    private String requestPhone = "";
    private String requestVCode = "";

    ReGetVerifyCodeButtonController reGetVerifyCodeButtonController;
    SmsVerificationCode smsObserver;

    @Override
    protected void setFragmentContent(Context content, ViewGroup container, Fragment titleBarFragment) {
        View root = LayoutInflater.from(content).inflate(R.layout.qy_sdk_fragment_bind_phone,container,false);
        phoneEdt = (EditText)root.findViewById(R.id.edt_bind_phone);
        vCodeEdt = (EditText)root.findViewById(R.id.edt_bind_phone_vcode);
        vCodeGetBtn = (Button)root.findViewById(R.id.btn_verification_code_get);
        confirmBtn = (Button)root.findViewById(R.id.btn_confirm);

        loadingDialog = new LoadingDialog(getActivity());
        reGetVerifyCodeButtonController = new ReGetVerifyCodeButtonController(vCodeGetBtn);
        smsObserver = new SmsVerificationCode(new Handler());
        getContext().getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);

        phoneEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vCodeGetBtn.setEnabled(s.length()>0);
                updateConfirmBtnState();
            }
        });
        vCodeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateConfirmBtnState();
            }
        });
        vCodeGetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneEdt.length() != 11){
                    ToastUtils.showMsg(R.string.please_input_valid_number);
                }else {
                    getVerificationCodeButtonImpl(phoneEdt.getText().toString());
                }
            }
        });
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput(phoneEdt.getText().toString(), vCodeEdt.getText().toString())){
                    bindPhone();
                }
            }
        });
        ViewUtils.bindEditWithButton(vCodeEdt,confirmBtn);
        container.addView(root);

    }

    private void updateConfirmBtnState(){
        confirmBtn.setEnabled(phoneEdt.length()>0 && vCodeEdt.length()>0);
    }

    private boolean checkInput(String phone, String vCode){
        if (phone.length() != 11){
            ToastUtils.showMsg(R.string.please_input_valid_number);
            return false;
        }
        putRequestInput(phone,vCode);
        return true;
    }

    private void putRequestInput(String phone, String vCode){
        requestPhone = phone;
        requestVCode = vCode;
    }

    private void clearRequestInput(){
        requestPhone = "";
        requestVCode = "";
    }

    private void bindPhone(){
        if (StringUtils.isBlank(requestPhone)|| StringUtils.isBlank(requestVCode)){
            ToastUtils.showMsg("请不要输入空字符");
            return;
        }
        loadingDialog.show();
        ApiFacade.getInstance().bindPhone(requestPhone,requestVCode,new QyRespListener(){
            @Override
            public void onNetSucc(String url, Map params, Object result) {
                super.onNetSucc(url, params, result);
                ToastUtils.showMsg(R.string.bind_phone_succ);
                AccountHistoryInfo accountHistoryInfo = ApiFacade.getInstance().getCurrentHistoryAccount();
                accountHistoryInfo.phone = requestPhone;
                ApiFacade.getInstance().insertOrUpdateAccountHistory(accountHistoryInfo);
                clearRequestInput();
                EventDispatcherAgent.defaultAgent().broadcast(FinishFragmentEvent.TYPE_FINISH_FRAGMENT,
                        new FinishFragmentEvent.Param(0, getActivity(), getParentFragment()));
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
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 获取短信验证码
     *
     * @param phone 手机号码
     */
    private void getVerificationCodeButtonImpl(String phone) {
        loadingDialog.show();
        ApiFacade.getInstance().requestVerificationCode(phone, IAuthApi.VCODE_TYPE_BIND_POHNE, retryTime, new QyRespListener<Void>() {
            @Override
            public void onNetSucc(String url, Map<String, String> params, Void result) {
                retryTime++;
                if (params != null) {
                    Log.d(DefaultUpdateImpl.TAG, "success request verify code ");
                    ToastUtils.showMsg(R.string.already_sent_verification_tips);
                    reGetVerifyCodeButtonController.prepare();
                    reGetVerifyCodeButtonController.startCountDown();
                } else {
                    Log.d(DefaultUpdateImpl.TAG, "error request verify code.");
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
            }

            @Override
            public void onNetworkComplete() {
                super.onNetworkComplete();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    protected Fragment getTitleBarFragment() {
        return CommonTitlePrimaryFragment.newInstance(getString(R.string.bind_phone));

    }

    @Override
    public NativeTitleBarUpdateInfo getTitleBarConfig() {
        NativeTitleBarUpdateInfo info = new NativeTitleBarUpdateInfo();
        info.showBackButton = true;
        info.showCloseButton = false;
//        info.showRefreshButton = false;
        info.showConfirmButton = false;
        return info;
    }

    @Override
    public void onPause() {
//        reGetVerifyCodeButtonController.cancelCountDown();
        getContext().getContentResolver().unregisterContentObserver(smsObserver);
        super.onPause();
    }

    private class ReGetVerifyCodeButtonController {
        TextView textView;

        public ReGetVerifyCodeButtonController(TextView textView) {
            this.textView = textView;
        }

        public void prepare() {
            ViewUtils.setViewEnable(textView, false);
        }

        private CountDownTimer timer;

        public void startCountDown() {
            if (timer != null) {
                return;
            }
            timer = new CountDownTimer(60 * 1000l, 1000l) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (textView.getVisibility() == VISIBLE){
                        int sec = (int) (millisUntilFinished / 1000l);
                        textView.setText(ResourceHelper.getString(R.string.re_get_verification_fmt, sec));
                    }
                }

                @Override
                public void onFinish() {
                    timer = null;
                    textView.setText(ResourceHelper.getString(R.string.re_get_verification));
                    ViewUtils.setViewEnable(textView, true);
                }
            };
            timer.start();
        }

        public void cancelCountDown() {
            if (timer != null) {
                timer.cancel();
                timer = null;
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(ResourceHelper.getString(R.string.re_get_verification));
                        ViewUtils.setViewEnable(textView, true);
                    }
                });
            }
        }

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
            ContentResolver cr = getContext().getContentResolver();
            String[] projection = new String[]{"body"};
            // 读取短信内容
            cursor = cr.query(SMS_ALL, projection, null, null, null);
            if (null == cursor) {
                return;
            }
            if (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body")); //获取短信的内容
                //根据正则分组表达式获取验证码
                Pattern pattern = Pattern.compile("(【QY语音】|【趣丸网】)(\\s*验证码\\s*)([0-9]{4})");
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    vCodeEdt.setText(matcher.group(3));
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
