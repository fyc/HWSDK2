package com.yiyou.gamesdk.core.base.http.volley.bean;

import com.yiyou.gamesdk.model.AuthModel;

public class LoginBean {


    /**
     * code : 1
     * data : {"user_id":72,"mobile_phone":12312312316,"token":"04ad9e20a0514a6a9184651acfdc24e8u1oTJ09k","need_real":"1"}
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
        /** 手机登录的bean
         * user_id : 72
         * mobile_phone : 12312312316
         * token : 04ad9e20a0514a6a9184651acfdc24e8u1oTJ09k
         * need_real : 1
         */
        /** 游客登录的bean
         * account_id : 1316
         * user_id : 290
         * guest : 1
         * first : 1
         * token : a1e565a774c34867bc9662ef40681d79sxf8e1iI
         */
        private int user_id = 0;
        private long mobile_phone = 0;
        private String token = "";
        private String need_real = "";
        private int account_id = 0;
        private String guest = "";
        private String first = "";

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public long getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(long mobile_phone) {
            this.mobile_phone = mobile_phone;
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


        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public String getGuest() {
            return guest;
        }

        public void setGuest(String guest) {
            this.guest = guest;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "user_id=" + user_id +
                    ", mobile_phone=" + mobile_phone +
                    ", token='" + token + '\'' +
                    ", need_real='" + need_real + '\'' +
                    ", account_id=" + account_id +
                    ", guest='" + guest + '\'' +
                    ", first='" + first + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "code=" + code +
                ", data=" + data.toString() +
                ", msg='" + msg + '\'' +
                '}';
    }

    public AuthModel convertedToAuthModel() {
        AuthModel authModel = new AuthModel();
        authModel.setUserID(((long) getData().getUser_id()));
        authModel.setPhone(getData().getMobile_phone() + "");
        authModel.setAccessToken(getData().getToken());
        authModel.setRealVerified(getData().getNeed_real());
        return authModel;
    }
}
