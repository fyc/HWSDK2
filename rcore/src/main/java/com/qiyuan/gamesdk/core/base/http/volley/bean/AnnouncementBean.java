package com.qiyuan.gamesdk.core.base.http.volley.bean;

import com.qiyuan.gamesdk.model.AnnouncementInfo;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementBean {

    /**
     * code : 1
     * data : {"bulletin":[{"url":"www.xxx.com"},{"url":"www.lj.com"}]}
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
        private List<BulletinBean> bulletin;

        public List<BulletinBean> getBulletin() {
            return bulletin;
        }

        public void setBulletin(List<BulletinBean> bulletin) {
            this.bulletin = bulletin;
        }

        public static class BulletinBean {
            /**
             * url : www.xxx.com
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public List<AnnouncementInfo> covertToInfo() {
        List<AnnouncementInfo> results = new ArrayList<>();
        for (DataBean.BulletinBean bulletinBean : getData().getBulletin()) {
            AnnouncementInfo infoBf = new AnnouncementInfo();
            infoBf.setUrl(bulletinBean.getUrl());
            infoBf.setTitle("公告");
            infoBf.setType(AnnouncementInfo.NORMAL);
            results.add(infoBf);
        }
        return results;
    }
}
