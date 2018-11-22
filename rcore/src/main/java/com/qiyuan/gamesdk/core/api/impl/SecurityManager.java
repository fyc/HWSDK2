package com.qiyuan.gamesdk.core.api.impl;

import android.support.v4.util.ArrayMap;

import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.def.ISecurityApi;
import com.qiyuan.gamesdk.core.base.http.RequestHelper;
import com.qiyuan.gamesdk.core.base.http.RequestManager;
import com.qiyuan.gamesdk.core.base.http.utils.Urlpath;
import com.qiyuan.gamesdk.core.base.http.volley.HwRequest;
import com.qiyuan.gamesdk.core.base.http.volley.QyLoginRequest;
import com.qiyuan.gamesdk.core.base.http.volley.bean.BindPhoneBean;
import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.util.ByteUtils;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.ISecurityApi;
import com.qiyuan.gamesdk.core.base.http.RequestHelper;
import com.qiyuan.gamesdk.core.base.http.RequestManager;
import com.qiyuan.gamesdk.core.base.http.utils.Urlpath;
import com.qiyuan.gamesdk.core.base.http.volley.HwRequest;
import com.qiyuan.gamesdk.core.base.http.volley.QyLoginRequest;
import com.qiyuan.gamesdk.core.base.http.volley.bean.BindPhoneBean;
import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.StringUtils;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.util.ByteUtils;
import com.qiyuan.gamesdk.util.ToastUtils;

import java.util.Map;

/**
 * Created by win on 15/6/8.
 * 修改密码、绑定手机、重置密码
 */
class SecurityManager implements ISecurityApi {
    private static final String TAG = "QYSDK:SecurityManager ";

    @Override
    public void modifyPayPassword(String oldPwd, String newPwd, QyRespListener callback) {
        if (StringUtils.isBlank(oldPwd)) {
            Log.e(TAG, "Error ModifyPassword. Old password is null or empty");
            return;
        }
        if (StringUtils.isBlank(newPwd)) {
            Log.e(TAG, "Error ModifyPassword. New password is null or empty");
            return;
        }
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("oldpwd", ByteUtils.generateMd5(oldPwd));
        params.put("newpwd",ByteUtils.generateMd5(newPwd));
        HwRequest hwRequest = new HwRequest<>(Urlpath.MODIFY_PAY_PWD,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void setPayPassword(String password, QyRespListener callback) {
        if (StringUtils.isBlank(password)) {
            Log.e(TAG, "Error setPayPassword. password is null or empty");
            return;
        }
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("accessToken", ApiFacade.getInstance().getSession());
        params.put("password",ByteUtils.generateMd5(password));
        HwRequest hwRequest = new HwRequest<>(Urlpath.SET_PAY_PWD,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void modifyPassword(String oldPwd, String newPwd, QyRespListener callback) {
        if (StringUtils.isBlank(oldPwd)) {
            Log.e(TAG, "Error ModifyPassword. Old password is null or empty");
            return;
        }
        if (StringUtils.isBlank(newPwd)) {
            Log.e(TAG, "Error ModifyPassword. New password is null or empty");
            return;
        }
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("oldpwd",ByteUtils.generateMd5(oldPwd));
        params.put("newpwd",ByteUtils.generateMd5(newPwd));
        HwRequest hwRequest = new HwRequest<>(Urlpath.MODIFY_PWD,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);

    }

    @Override
    public void forgetPayPassword(String mobile, String newPwd, String vcode, QyRespListener callback) {
        if (StringUtils.isBlank(mobile)||StringUtils.isBlank(newPwd)||StringUtils.isBlank(vcode) ) {
            ToastUtils.showMsg("请不要输入空字符");
            return;
        }
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("phone",mobile);
        params.put("newpwd",ByteUtils.generateMd5(newPwd));
        params.put("vcode",vcode);
        HwRequest hwRequest = new HwRequest<>(Urlpath.MODIFY_PAY_PWD,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void forgetPassword(String mobile, String newPwd, String vcode, QyRespListener callback) {
        if (StringUtils.isBlank(mobile)||StringUtils.isBlank(newPwd)||StringUtils.isBlank(vcode) ) {
            ToastUtils.showMsg("请不要输入空字符");
            return;
        }
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("phone",mobile);
        params.put("newpwd",ByteUtils.generateMd5(newPwd));
        params.put("vcode",vcode);
        HwRequest hwRequest = new HwRequest<>(Urlpath.MODIFY_PWD,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void bindPhone(String phoneNum, String smsVCode, QyRespListener callback) {
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("vcode",smsVCode);
        params.put("mobile",phoneNum);
        HwRequest hwRequest = new HwRequest<>(Urlpath.BIND_PHONE,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }
    @Override
    public void bindPhone2(String phoneNum, String smsVCode, QyRespListener callback) {
        Map<String, String> params = new ArrayMap<>();
        AccountHistoryInfo info = ApiFacade.getInstance().getCurrentHistoryAccount();
        String user_id = info.userID+"";
        String token = info.accessToken;
//        String game_id = QyLoginRequest.GAMW_ID;
        String game_id = ApiFacade.getInstance().getCurrentGameID();
        String ctime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("user_id", user_id);
        params.put("token", token);
        params.put("mobile_phone", phoneNum);
        params.put("game_id", game_id);
        params.put("ctime", ctime);
        params.put("code", smsVCode);
        String src = String.format("code=%s&ctime=%s&game_id=%s&mobile_phone=%s&token=%s&user_id=%s", smsVCode, ctime, game_id, phoneNum,token,user_id);
        params.put("src", src);

        QyLoginRequest hwRequest = new QyLoginRequest(Urlpath.BIND_PHONE, params, BindPhoneBean.class, callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void unbindPhone(String phoneNum, String smsVCode, QyRespListener callback) {
        if (StringUtils.isBlank(phoneNum)||StringUtils.isBlank(smsVCode)){
            ToastUtils.showMsg("请不要输入空字符");
            return;
        }
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("vcode",smsVCode);
        params.put("mobile",phoneNum);
        HwRequest hwRequest = new HwRequest<>(Urlpath.UNBIND_PHONE,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void realNameAuth(String real_name, String card_no, QyRespListener<QyDataBean> callback) {
        Map<String, String> params = new ArrayMap<>();
        AccountHistoryInfo info = ApiFacade.getInstance().getCurrentHistoryAccount();
        String user_id = info.userID+"";
        String token = info.accessToken;
        String mobile_phone = info.phone;
//        String game_id = QyLoginRequest.GAMW_ID;
        String game_id = ApiFacade.getInstance().getCurrentGameID();
        String ctime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("user_id", user_id);
        params.put("token",token );
        params.put("real_name", real_name);
        params.put("mobile_phone", mobile_phone);
        params.put("ctime", ctime);
        params.put("game_id", game_id);
        params.put("card_no", card_no);
        String src = String.format("card_no=%s&ctime=%s&game_id=%s&mobile_phone=%s&real_name=%s&token=%s&user_id=%s",
                card_no, ctime, game_id, mobile_phone,real_name,token,user_id);
        params.put("src", src);

        QyLoginRequest hwRequest = new QyLoginRequest(Urlpath.REALNAME_INFO, params, QyDataBean.class, callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }

    @Override
    public void verifyPayPassword(String payPassword, QyRespListener callback) {
        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        params.put("password",ByteUtils.generateMd5(payPassword));
        params.put("accessToken",ApiFacade.getInstance().getSession());
        HwRequest hwRequest = new HwRequest<>(Urlpath.VERIFY_PAY_PASSWORD,params,null,callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);
    }
}
