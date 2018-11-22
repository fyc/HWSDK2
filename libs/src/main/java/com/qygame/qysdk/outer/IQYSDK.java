package com.qygame.qysdk.outer;

import android.app.Activity;
import android.content.Context;

import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.model.PaymentInfo;
import com.qygame.qysdk.outer.model.VersionDir;

import java.util.Map;

/**
 * Created by Win on 2017/4/13.
 */
public interface IQYSDK {

    void attach(ContextWrapper contextWrapper, VersionDir versionDir);


    /**
     * @param context
     * @param info
     * @param orientation
     * @param callback
     */
    void init(Activity context, GameParamInfo info, boolean isDebug, int orientation, IOperateCallback<String> callback);


    /**
     * @param context
     * @param callback
     */
    void uninit(Context context, IOperateCallback<String> callback);

    /**
     * @param activity
     * @param callback
     */
    void login(Activity activity, IOperateCallback<String> callback);

    void loginVisitors(Activity activity, IOperateCallback<String> callback);

    void loginAuto(Activity activity, IOperateCallback<String> callback);

    /**
     * 设置登出回调
     *
     * @param callback
     */
    void setLogoutListener(IOperateCallback<String> callback);

    void logout();

    void showFloatView(Activity activity);

    void hideFloatView(Activity activity);


    /**
     * pay
     *
     * @param activity
     * @param callback
     */
    void pay(Activity activity, PaymentInfo payInfo, IOperateCallback<String> callback);

//    void payH5(Activity activity, String referer, String payUrl, IOperateCallback<String> callback);

    void payH5(Activity activity, Long cliBuyerId, String cliSellerId, String cpOrderNo, String cpOrderTitle, float cpPrice);

    /**
     * 判断是否登陆
     *
     * @return
     */
    boolean isLogin();

    /**
     * 获取gameID
     *
     * @return gameId
     */
    String getGameId();

    /***
     * 获取uid
     *
     * @return uid
     */
    String getUid();

    /**
     * 获取session
     *
     * @return session
     */
    String getSession();

    /**
     * 上报角色信息
     *
     * @param serverName
     * @param roleID
     * @param roleName
     * @param roleLevel
     * @param type       提交场景："create"表示角色创建时；
     *                   "enter"表示进入服务器时；
     *                   "upgrade"表示等级提升时
     */
    void submitGameRoleInfo(Activity activity, String type, String serverName, String roleID, String roleName, int roleLevel, String exInfo);

    /***
     * 上报在线事件到服务器
     *
     * @param params
     */
    void submitExtendData(Activity activity, Map<String, String> params);
}
