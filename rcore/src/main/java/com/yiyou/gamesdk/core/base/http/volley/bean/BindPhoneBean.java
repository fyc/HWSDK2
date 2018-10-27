package com.yiyou.gamesdk.core.base.http.volley.bean;

public class BindPhoneBean {

    /**
     * code : 1
     * data : {"user_id":22,"token":"b98d6344e0a84da2a2e4130612c53714a5HqSJ9J"}
     * msg : 绑定成功
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
         * user_id : 22
         * token : b98d6344e0a84da2a2e4130612c53714a5HqSJ9J
         */

        private int user_id;
        private String token;

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
    }
}
