package com.qiyuan.gamesdk.core.api.def;

import android.app.Activity;

import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;

import java.util.Map;

/**
 * Created by chenshuide on 15/6/8.
 */
public interface IReportApi extends IApiWrapping {

    void feedback(String content, QyRespListener<Void> callback);

    // 激活事件请求
    void reportActivate(Activity activity,IOperateCallback<QyDataBean> callback);

    void onLineEvent( IOperateCallback<Void> callback);

    void onCharacterEvent(Map<String, String> params, IOperateCallback<Void> callback);
}
