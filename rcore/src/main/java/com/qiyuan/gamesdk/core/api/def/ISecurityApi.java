package com.qiyuan.gamesdk.core.api.def;

import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;

/**
 * Created by win on 17/4/26.
 */
public interface ISecurityApi extends IApiWrapping {

    void setPayPassword(String password, QyRespListener callback);

    void modifyPayPassword(String oldPwd, String newPwd, QyRespListener callback);

    void modifyPassword(String oldPwd, String newPwd, QyRespListener callback);

    void forgetPayPassword(String mobile, String newPwd, String vcode,QyRespListener callback);

    void forgetPassword(String mobile, String newPwd, String vcode,QyRespListener callback);

    void bindPhone(String phoneNum, String smsVCode, QyRespListener callback);
    void bindPhone2(String phoneNum, String smsVCode, QyRespListener callback);

    void unbindPhone(String phoneNum, String smsVCode, QyRespListener callback);

    void verifyPayPassword(String payPassword, QyRespListener callback);
    void realNameAuth(String real_name, String card_no, QyRespListener<QyDataBean> callback);
}
