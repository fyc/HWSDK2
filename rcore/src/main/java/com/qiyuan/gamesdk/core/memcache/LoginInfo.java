package com.qiyuan.gamesdk.core.memcache;

import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.model.AuthModel;

/**
 * Created by charles on 12/7/16.
 */
public class LoginInfo {


    public static LoginInfo instance;
    private AuthModel authModel;
    private LoginBean loginBean;

    private LoginInfo() {

    }

    public static LoginInfo getInstance() {
        if (instance == null) {
            instance = new LoginInfo();
        }
        return instance;
    }

    public void setAuthModel(AuthModel authModel) {
        this.authModel = authModel;
    }

    public AuthModel getAuthModel() {
        return authModel;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void clear() {
        loginBean = null;
        authModel = null;
        instance = null;
    }
}
