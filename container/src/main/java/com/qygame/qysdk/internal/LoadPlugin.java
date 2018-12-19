package com.qygame.qysdk.internal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.qygame.qysdk.outer.ContextWrapper;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.IQYSDK;
import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.model.PaymentInfo;
import com.qygame.qysdk.outer.model.RootDir;
import com.qygame.qysdk.outer.model.VersionDir;

import java.util.Map;

/**
 * Created by Win on 2017/4/13.
 */
public class LoadPlugin {

    private static LoadPlugin instance = null;
    private ContextWrapper contextWrapper;
    private static final String ENTER_CLASS_NAME = "com.qiyuan.gamesdk.qycore.QYSDKImpl";
    private IQYSDK QYSDKApi;
    private boolean isInit = false;

    private LoadPlugin() {
    }

    public static LoadPlugin getInstance() {
        if (instance == null)
            instance = new LoadPlugin();
        return instance;
    }


    public void init(Activity context, GameParamInfo info, boolean isDebug, int orientation, final IOperateCallback<String> callback) {
        Log.d("LoadPlugin", "init: " + isInit);
        if (isInit) {
            callback.onResult(0, "sdk already init");
            return;
        }
        QYVersionManager qyVersionManager = new QYVersionManager(context, RootDir.getInstance(context));
        VersionDir versionDir = qyVersionManager.getVersionPath();
        contextWrapper = new ContextWrapper(context, versionDir);
        contextWrapper.init();
        try {
            Class<?> localClass = contextWrapper.getClassLoader().loadClass(ENTER_CLASS_NAME);
            QYSDKApi = (IQYSDK) localClass.newInstance();
            QYSDKApi.attach(contextWrapper, versionDir);
            QYSDKApi.init(context, info, isDebug, orientation, new IOperateCallback<String>() {
                @Override
                public void onResult(int i, String s) {
                    if (i == 0) {
                        isInit = true;
                    }
                    callback.onResult(i, s);
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void uninit(Context context, IOperateCallback<String> callback) {
        if (isInit) {
            QYSDKApi.uninit(context, callback);
        }
    }

    public void login(Activity activity, IOperateCallback<String> callback) {
        if (isInit) {
            QYSDKApi.login(activity, callback);
        }
    }

    public void loginVisitors(Activity activity, IOperateCallback<String> callback) {
        if (isInit) {
            QYSDKApi.loginVisitors(activity, callback);
        }
    }

    public void loginAuto(Activity activity, IOperateCallback<String> callback) {
        if (isInit) {
            QYSDKApi.loginAuto(activity, callback);
        }
    }

    public void setLogoutListener(IOperateCallback<String> callback) {
        if (isInit) {
            QYSDKApi.setLogoutListener(callback);
        }
    }

    public void logout() {
        if (isInit) {
            QYSDKApi.logout();
        }
    }

    public void showFloatView(Activity activity) {
        if (isInit) {
            QYSDKApi.showFloatView(activity);
        }
    }

    public void hideFloatView(Activity activity) {
        if (isInit) {
            QYSDKApi.hideFloatView(activity);
        }
    }
    public void showFloatView2(Activity activity) {
        if (isInit) {
            QYSDKApi.showFloatView2(activity);
        }
    }

    public void hideFloatView2(Activity activity) {
        if (isInit) {
            QYSDKApi.hideFloatView2(activity);
        }
    }
    public void pay(Activity activity, PaymentInfo payInfo, IOperateCallback<String> callback) {
        if (isInit) {
            QYSDKApi.pay(activity, payInfo, callback);
        }
    }

    public void payH5(Activity activity, Long cliBuyerId, String cliSellerId, String cpOrderNo, String cpOrderTitle, float cpPrice) {
        if (isInit) {
            QYSDKApi.payH5(activity, cliBuyerId, cliSellerId, cpOrderNo, cpOrderTitle, cpPrice);
        }
    }

    public boolean isLogin() {
        if (isInit) {
            return QYSDKApi.isLogin();
        }
        return false;
    }

    public String getGameId() {
        if (isInit) {
            return QYSDKApi.getGameId();
        }
        return "";
    }

    public String getUid() {
        if (isInit) {
            return QYSDKApi.getUid();
        }
        return "";
    }

    public String getSession() {
        if (isInit) {
            return QYSDKApi.getSession();
        }
        return "";
    }

    public void submitGameRoleInfo(Activity activity, String type, String serverName, String roleID, String roleName, int roleLevel, String exInfo) {
        if (isInit) {
            QYSDKApi.submitGameRoleInfo(activity, type, serverName, roleID, roleName, roleLevel, exInfo);
        }

    }

    public void submitExtendData(Activity activity, Map<String, String> params) {
        if (isInit) {
            QYSDKApi.submitExtendData(activity, params);
        }

    }
}
