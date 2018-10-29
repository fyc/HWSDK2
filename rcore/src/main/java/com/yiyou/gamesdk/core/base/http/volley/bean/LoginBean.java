package com.yiyou.gamesdk.core.base.http.volley.bean;

import com.mobilegamebar.rsdk.outer.util.Log;
import com.yiyou.gamesdk.model.AuthModel;

public class LoginBean {

    /**
     * code : 1
     * data : {"user_id":72,"token":"04ad9e20a0514a6a9184651acfdc24e8u1oTJ09k","need_real":"1"}
     * msg : 成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * user_id : 72
         * token : 04ad9e20a0514a6a9184651acfdc24e8u1oTJ09k
         * need_real : 1
         */

        private int user_id;
        private String token;
        private String need_real;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNeed_real() {
            return need_real;
        }

        public void setNeed_real(String need_real) {
            this.need_real = need_real;
        }
    }

//    @Override
//    public String toString() {
//        return "LoginBean{" +
//                "code=" + code +
//                ", data=" + data +
//                ", msg='" + msg + '\'' +
//                '}';
//    }

    public  AuthModel convertedToAuthModel() {
        AuthModel authModel = new AuthModel();
        authModel.setUserID(getData().getUser_id());
        authModel.setAccessToken(getData().getToken());
        authModel.setRealVerified(getData().getNeed_real());
        return authModel;
    }
}
