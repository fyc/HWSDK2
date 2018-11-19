package com.yiyou.gamesdk.core.api.impl;


import android.app.Activity;
import android.os.Build;

import com.qygame.qysdk.outer.IOperateCallback;
import com.yiyou.gamesdk.core.CoreManager;
import com.yiyou.gamesdk.core.api.ApiFacade;
import com.yiyou.gamesdk.core.api.def.IReportApi;
import com.yiyou.gamesdk.core.base.http.RequestHelper;
import com.yiyou.gamesdk.core.base.http.RequestManager;
import com.yiyou.gamesdk.core.base.http.utils.NetworkUtil;
import com.yiyou.gamesdk.core.base.http.utils.Urlpath;
import com.yiyou.gamesdk.core.base.http.volley.HwRequest;
import com.yiyou.gamesdk.core.base.http.volley.QyLoginRequest;
import com.yiyou.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.yiyou.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.yiyou.gamesdk.util.Base64;
import com.yiyou.gamesdk.util.PhoneUtils;
import com.yiyou.gamesdk.util.QiyuanSignUtils;
import com.yiyou.gamesdk.util.ScreenUtil;
import com.yiyou.gamesdk.util.TimeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

/**
 * Created by levyyoung on 15/6/8.R
 * 上报游戏、角色信息
 */
class ReportManager implements IReportApi {

    private final static String TAG = "RSDK: " + ReportManager.class.getSimpleName();

    private final static String TYPE_ACTIVATE = "ACTIVATE";
    private final static String TYPE_ONLINE = "ONLINE";
    private final static String TYPE_CHARACTER = "CHARACTER";

    @Override
    public void feedback(String content, QyRespListener<Void> callback) {
        Map<String, String> params = new TreeMap<>();
        params.put("content", content);
        RequestHelper.buildParamsWithBaseInfo(params);
        HwRequest<Void> request = new HwRequest<>(Urlpath.FEEDBACK, params, null, callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(request, null);
    }

    // 激活事件请求
    @Override
    public void reportActivate(Activity activity, final IOperateCallback<QyDataBean> callback) {
        Map<String, String> params = new TreeMap<>();
        String platform_id = "2";
        String product_slug = ApiFacade.getInstance().getCurrentGameID();
        String channel_name = "10000";
        String model = Build.MODEL;
        String network_code = NetworkUtil.getProviderCode(CoreManager.getContext());
        String network_type = NetworkUtil.getNetworkTypeInt(CoreManager.getContext()) + "";
        String device_id = PhoneUtils.getDeviceId(CoreManager.getContext());
        String resolution = ScreenUtil.getPixels(activity);
        String os = "android " + android.os.Build.VERSION.RELEASE;
        String ctime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("event_type", "device");
        params.put("platform_id", platform_id);
        params.put("product_slug", product_slug);
        params.put("channel_name", channel_name);
        params.put("model", model);
        params.put("network_code", network_code);
        params.put("network_type", network_type);
        params.put("device_id", device_id);
        params.put("resolution", resolution);
        params.put("os", os);
        params.put("ctime", ctime);
        String src = String.format("platform_id=%s&ctime=%s&product_slug=%s&channel_name=%s&" +
                "model=%s&network_code=%s&network_type=%s&device_id=%s&" +
                "resolution=%s&os=%s", platform_id, ctime, product_slug, channel_name, model, network_code, network_type, device_id, resolution, os);
        params.put("src", src);

        QyLoginRequest<QyDataBean> hwRequest = new QyLoginRequest<>(Urlpath.USER_ACTIVATION, params, QyDataBean.class, new QyRespListener<QyDataBean>() {

            @Override
            public void onNetSucc(String url, Map<String, String> params, QyDataBean bean) {
                super.onNetSucc(url, params, bean);
                callback.onResult(bean.getCode(), bean);
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                callback.onResult(Integer.valueOf(errno), null);
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                callback.onResult(errorNo, null);
            }
        });
        RequestManager.getInstance(CoreManager.getContext()).addRequest(hwRequest, null);

    }


    /**
     * 上报在线
     *
     * @param callback 回调接口
     */
    @Override
    public void onLineEvent(final IOperateCallback<Void> callback) {
        if (!ApiFacade.getInstance().isLogin()) {
            return;
        }
        Map<String, String> params = new HashMap<>();

        params.put("timestamp", String.valueOf(new Date().getTime()));
        params.put("onlineTime", "600");
        RequestHelper.buildParamsWithBaseInfo(params);
        QiyuanSignUtils.addSign(params);

        HwRequest<Void> request = new HwRequest<>(Urlpath.REPORT, params, Void.class, new QyRespListener<Void>() {
            @Override
            public void onNetSucc(String url, Map<String, String> params, Void result) {
                if (callback != null)
                    callback.onResult(0, result);
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                if (CoreManager.isDebug()) {
                    super.onNetError(url, params, errno, errmsg);
                }
                if (callback != null)
                    callback.onResult(0, null);
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                if (CoreManager.isDebug()) {
                    super.onFail(errorNo, errmsg);
                }
                if (callback != null)
                    callback.onResult(errorNo, null);
            }
        });

        RequestManager.getInstance(CoreManager.getContext()).addRequest(request, null);

    }

    /**
     * 上报英雄属性
     *
     * @param params   参数
     * @param callback callback
     */
    @Override
    public void onCharacterEvent(Map<String, String> params, final IOperateCallback<Void> callback) {
        if (params == null)
            return;
        params.put("type", TYPE_CHARACTER);
        params.put("ctime", TimeUtils.formatNowTime());
        RequestHelper.buildParamsWithBaseInfo(params);

//        QyRequest request = new QyRequest<>(Urlpath.REPORT, params, Void.class, new QyRespListener<Void>() {
//            @Override
//            public void onNetSucc(String url, Map<String, String> params, Void result) {
//                if (callback != null)
//                    callback.onResult(0, result);
//            }
//
//            @Override
//            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
//                if (CoreManager.isDebug()) {
//                    super.onNetError(url, params, errno, errmsg);
//                }
//                //fixme
//                if (callback != null)
//                    callback.onResult(0, null);
//            }
//
//            @Override
//            public void onFail(int errorNo, String errmsg) {
//                if (CoreManager.isDebug()) {
//                    super.onFail(errorNo, errmsg);
//                }
//                if (callback != null)
//                    callback.onResult(errorNo, null);
//            }
//        });
//
//        RequestManager.getInstance(CoreManager.getContext()).addRequest(request, null);

    }

    private String getFileContent(File file) {
        try {

            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(os);

            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data, 0, len);
            gzip.write(data);

            gzip.flush();
            gzip.close();
            fis.close();

            return Base64.encode(os.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

}
