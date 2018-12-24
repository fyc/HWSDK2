package com.qiyuan.gamesdk.qycore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;

import com.qiyuan.gamesdk.PluginManager;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.interceptor.Interceptor;
import com.qiyuan.gamesdk.core.interceptor.LoginInterceptor;
import com.qiyuan.gamesdk.core.interceptor.LoginNoticeInterceptor;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.floatview.FloatViewManager;
import com.qiyuan.gamesdk.core.ui.widget.ExitAlertDialogView;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.util.PermissionHelper;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qygame.qysdk.outer.ContextWrapper;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.IQYSDK;
import com.qygame.qysdk.outer.QYSDKSpace;
import com.qygame.qysdk.outer.event.AuthEvent;
import com.qygame.qysdk.outer.event.EventDispatcherAgent;
import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.model.PaymentInfo;
import com.qygame.qysdk.outer.model.VersionDir;
import com.qygame.qysdk.outer.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Win on 2017/4/13.
 */
public class QYSDKImpl implements IQYSDK {

    private static final String TAG = "QYSDK:QYSDKImpl";
    private IOperateCallback<String> logoutCallback;

    @Override
    public void attach(ContextWrapper contextWrapper, VersionDir versionDir) {
        PluginManager.getInstance().attach(contextWrapper, versionDir);
    }

    @Override
    public void init(Activity context, GameParamInfo rGameInfo, boolean debugMode, int orientation, IOperateCallback<String> qysdkCallback) {
        PluginManager.getInstance().init(context, debugMode, rGameInfo, orientation, qysdkCallback);
    }

    @Override
    public void uninit(final Context context, final IOperateCallback<String> qysdkCallback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ExitAlertDialogView alertDialogView = ViewControllerNavigator.getInstance().toExitAlertDialogView(context);
                alertDialogView.setMessageTip(R.string.confirm_exit);
                alertDialogView.setEnsureText(R.string.exit);
                alertDialogView.setListener(new ExitAlertDialogView.DialogClickListener() {
                    @Override
                    public void onEnsureClick() {
                        QYSDKSpace.getInstance(context.getApplicationContext()).uninit();
                        exit();
                        PluginManager.getInstance().uninit(qysdkCallback);
                    }
                });
            }
        });

    }

    private void exit() {
        Log.d(TAG, "exit");
        EventDispatcherAgent.defaultAgent().removeAllEventListeners();
        try {
            Field sConstructorMapField = LayoutInflater.class.getDeclaredField("sConstructorMap");
            sConstructorMapField.setAccessible(true);
            Object o = sConstructorMapField.get(null);
            if (o != null && o instanceof HashMap) {
                HashMap sConstructorMap = (HashMap) o;
                sConstructorMap.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        logoutCallback = null;
    }

    @Override
    public void login(Activity activity, final IOperateCallback<String> callback) {

        if (!PermissionHelper.Storage.hasStoragePermission(activity)) {
            ToastUtils.showMsg("未获得文件存储权限，请前往权限管理打开。");
            callback.onResult(1, "登录失败");
            return;
        }
        ApiFacade.getInstance().setupChannelInfo();

        final AuthEvent.LoginParams loginParams = new AuthEvent.LoginParams(activity, callback, "QYSDK");
        final AuthEvent.LoginParams loginParamsProxy = new AuthEvent.LoginParams(activity,
                new IOperateCallback<String>() {
                    @Override
                    public void onResult(int i, String s) {
                        Log.d(TAG, "Chain:i=" + i + "--s=" + s);
                        List<Interceptor> interceptors = new ArrayList<>();
                        interceptors.add(new LoginNoticeInterceptor());
                        LoginInterceptor.LoginParams params = new LoginInterceptor.LoginParams(loginParams, i, s);
                        LoginChain loginChain = new LoginChain(params, 0, interceptors);
                        loginChain.proceed(params);
                    }
                }, "QYSDK");
        FloatViewManager.getInstance().hide();
//        ViewControllerNavigator.getInstance().toLogin(loginParamsProxy);
        ViewControllerNavigator.getInstance().beforeLogin(loginParams);
    }

    @Override
    public void regist(Activity activity, IOperateCallback<String> iOperateCallback) {
        if (!PermissionHelper.Storage.hasStoragePermission(activity)) {
            ToastUtils.showMsg("未获得文件存储权限，请前往权限管理打开。");
            iOperateCallback.onResult(1, "登录失败");
            return;
        }
        ApiFacade.getInstance().setupChannelInfo();

        final AuthEvent.LoginParams loginParams = new AuthEvent.LoginParams(activity, iOperateCallback, "QYSDK");
        FloatViewManager.getInstance().hide();
        ViewControllerNavigator.getInstance().toRegister2(loginParams);
    }

    @Override
    public void hasRegistAndToLogin(Activity activity, IOperateCallback<String> iOperateCallback) {
        if (!PermissionHelper.Storage.hasStoragePermission(activity)) {
            ToastUtils.showMsg("未获得文件存储权限，请前往权限管理打开。");
            iOperateCallback.onResult(1, "登录失败");
            return;
        }
        ApiFacade.getInstance().setupChannelInfo();

        final AuthEvent.LoginParams loginParams = new AuthEvent.LoginParams(activity, iOperateCallback, "QYSDK");
        FloatViewManager.getInstance().hide();
        ViewControllerNavigator.getInstance().toHasRegisterAndToLogin2(loginParams);
    }

    @Override
    public void hasRegistAndToSetPassword(Activity activity, IOperateCallback<String> iOperateCallback) {
        if (!PermissionHelper.Storage.hasStoragePermission(activity)) {
            ToastUtils.showMsg("未获得文件存储权限，请前往权限管理打开。");
            iOperateCallback.onResult(1, "登录失败");
            return;
        }
        ApiFacade.getInstance().setupChannelInfo();

        final AuthEvent.LoginParams loginParams = new AuthEvent.LoginParams(activity, iOperateCallback, "QYSDK");
        FloatViewManager.getInstance().hide();
        ViewControllerNavigator.getInstance().toHasRegisterAndToSetPassword2(loginParams);
    }

    @Override
    public void tologinPhone2(Activity activity, IOperateCallback<String> iOperateCallback) {
        if (!PermissionHelper.Storage.hasStoragePermission(activity)) {
            ToastUtils.showMsg("未获得文件存储权限，请前往权限管理打开。");
            iOperateCallback.onResult(1, "登录失败");
            return;
        }
        ApiFacade.getInstance().setupChannelInfo();

        final AuthEvent.LoginParams loginParams = new AuthEvent.LoginParams(activity, iOperateCallback, "QYSDK");
        FloatViewManager.getInstance().hide();
        ViewControllerNavigator.getInstance().tologinPhone2(loginParams);
    }

    @Override
    public void loginVisitors(Activity activity, final IOperateCallback<String> callback) {
    }

    @Override
    public void loginAuto(Activity activity, final IOperateCallback<String> callback) {
    }

    @Override
    public void setLogoutListener(IOperateCallback<String> qysdkCallback) {
        logoutCallback = qysdkCallback;
        PluginManager.getInstance().setLogoutCallback(qysdkCallback);
    }

    @Override
    public void logout() {
//        if (logoutCallback==null){
//            ApiFacade.getInstance().logout();
//        }else{
//            ApiFacade.getInstance().logout(logoutCallback);
//        }
        ApiFacade.getInstance().logout(logoutCallback);
    }

    @Override
    public void showFloatView(Activity activity) {
        PluginManager.getInstance().showFloatView();
    }

    @Override
    public void hideFloatView(Activity activity) {
        PluginManager.getInstance().hideFloatview();
    }

    @Override
    public void showFloatView2(Activity activity) {
        PluginManager.getInstance().showFloatView2();
    }

    @Override
    public void hideFloatView2(Activity activity) {
        PluginManager.getInstance().hideFloatview2();
    }

    @Override
    public void pay(Activity activity, PaymentInfo payOrderInfo, IOperateCallback<String> qysdkCallback) {
        ApiFacade.getInstance().order(payOrderInfo, activity, qysdkCallback);
    }

    @Override
    public void payH5(Activity activity, Long aLong, String s, String s1, String s2, float v) {
        ApiFacade.getInstance().orderH5(activity, aLong, s, s1, s2, v);
    }

    @Override
    public boolean isLogin() {
        return ApiFacade.getInstance().isLogin();
    }

    @Override
    public String getGameId() {
        return PluginManager.getInstance().getGameId() + "";
    }

    @Override
    public String getUid() {
        return PluginManager.getInstance().getUid() + "";
    }

    @Override
    public String getSession() {
        return PluginManager.getInstance().getSession();
    }

    @Override
    public void submitGameRoleInfo(Activity activity, String type, String serverName, String roleID, String roleName, int roleLevel, String exInfo) {
        Map<String, String> params = new TreeMap<>();
        params.put("serverName", serverName);
        params.put("roleID", roleID);
        params.put("roleName", roleName);
        params.put("level", String.valueOf(roleLevel));
        ApiFacade.getInstance().onCharacterEvent(params, null);

    }

    @Override
    public void submitExtendData(Activity activity, Map<String, String> map) {
        ApiFacade.getInstance().onCharacterEvent(map, null);
    }

    private boolean isHasAccountInLocal() {
        List<AccountHistoryInfo> AccountHistories = ApiFacade.getInstance().getAccountHistories();
        if (AccountHistories == null) {
            return false;
        }
        if (AccountHistories.isEmpty()) {
            return false;
        }
        return true;
    }

    private class LoginChain implements Interceptor.Chain<LoginInterceptor.LoginParams> {

        private LoginInterceptor.LoginParams params;
        private int index;
        private List<Interceptor> interceptors;

        public LoginChain(final LoginInterceptor.LoginParams params, int index, List<Interceptor> interceptors) {
            this.params = params;
            this.index = index;
            this.interceptors = interceptors;
        }

        @Override
        public LoginInterceptor.LoginParams getData() {
            return params;
        }

        @Override
        public void proceed(LoginInterceptor.LoginParams data) {

            if (index < interceptors.size()) {
                LoginChain loginChain = new LoginChain(data, index + 1, interceptors);
                LoginInterceptor interceptor = (LoginInterceptor) interceptors.get(index);
                interceptor.intercept(loginChain);
            } else {
                data.getLoginParams().callback.onResult(data.getCode(), data.getResult());
            }
        }
    }
}
