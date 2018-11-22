package com.qygame.qysdk.container;

import android.app.Activity;
import android.content.Context;

import com.qygame.qysdk.internal.LoadPlugin;
import com.qygame.qysdk.outer.ContextWrapper;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.IQYSDK;
import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.model.PaymentInfo;
import com.qygame.qysdk.outer.model.VersionDir;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.StorageConfig;

import java.util.Map;

/**
 * Created by Win on 2017/4/13.
 */
public class QYGameSDK implements IQYSDK {

    private static QYGameSDK instance = null;

    public static synchronized QYGameSDK getInstance() {
        if (instance == null) {
            instance = new QYGameSDK();
        }
        return instance;
    }


    @Override
    public void attach(ContextWrapper contextWrapper, VersionDir versionDir) {

    }

    /**
     * @param context     当前Activity
     * @param info        存储游戏信息，如gameId，sdkkey等内容
     * @param isDebug     调试模式，true：代表调试模式，false：代表生产模式
     * @param orientation 横竖屏模式 Configuration.ORIENTATION_LANDSCAPE、Configuration.ORIENTATION_PORTRAIT
     * @param callback    回调接口 i == 0 代表成功
     */
    @Override
    public void init(Activity context, GameParamInfo info, boolean isDebug, int orientation, IOperateCallback<String> callback) {
        StorageConfig.prepare(context, isDebug);
        Log.init(context, Log.LogLevel.Verbose, getLogDir(context, info.getGameId()));
        LoadPlugin.getInstance().init(context, info, isDebug, orientation, callback);
    }

    @Override
    public void uninit(Context context, IOperateCallback<String> callback) {
        LoadPlugin.getInstance().uninit(context, callback);
    }

    /**
     * @param activity
     * @param callback
     */
    @Override
    public void login(Activity activity, IOperateCallback<String> callback) {
        LoadPlugin.getInstance().login(activity, callback);
    }

    @Override
    public void loginVisitors(Activity activity, IOperateCallback<String> callback) {
        LoadPlugin.getInstance().loginVisitors(activity, callback);
    }

    @Override
    public void loginAuto(Activity activity, IOperateCallback<String> callback) {
        LoadPlugin.getInstance().loginAuto(activity, callback);
    }

    @Override
    public void setLogoutListener(IOperateCallback<String> callback) {
        LoadPlugin.getInstance().setLogoutListener(callback);
    }

    @Override
    public void logout() {
        LoadPlugin.getInstance().logout();
    }

    @Override
    public void showFloatView(Activity activity) {
        LoadPlugin.getInstance().showFloatView(activity);
    }

    @Override
    public void hideFloatView(Activity activity) {
        LoadPlugin.getInstance().hideFloatView(activity);
    }

    @Override
    public void pay(Activity activity, PaymentInfo payInfo, IOperateCallback<String> callback) {
        LoadPlugin.getInstance().pay(activity, payInfo, callback);
    }


    @Override
    public void payH5(Activity activity, Long cliBuyerId, String cliSellerId, String cpOrderNo, String cpOrderTitle, float cpPrice) {
        LoadPlugin.getInstance().payH5(activity, cliBuyerId, cliSellerId, cpOrderNo, cpOrderTitle, cpPrice);
    }


    @Override
    public boolean isLogin() {
        return LoadPlugin.getInstance().isLogin();
    }

    @Override
    public String getGameId() {
        return LoadPlugin.getInstance().getGameId();
    }

    @Override
    public String getUid() {
        return LoadPlugin.getInstance().getUid();
    }

    @Override
    public String getSession() {
        return LoadPlugin.getInstance().getSession();
    }

    @Override
    public void submitGameRoleInfo(Activity activity, String type, String serverName, String roleID, String roleName, int roleLevel, String exInfo) {
        LoadPlugin.getInstance().submitGameRoleInfo(activity, type, serverName, roleID, roleName, roleLevel, exInfo);
    }

    @Override
    public void submitExtendData(Activity activity, Map<String, String> params) {
        LoadPlugin.getInstance().submitExtendData(activity, params);
    }

    private String getLogDir(Context context, String gameId) {
        return StorageConfig.getGameLogDirPath(gameId);
    }
}
