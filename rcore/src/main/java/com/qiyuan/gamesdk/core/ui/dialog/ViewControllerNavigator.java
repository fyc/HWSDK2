package com.qiyuan.gamesdk.core.ui.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.ui.dialog.biz.AccountCenterViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.BindPhoneSuccessfulViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.BindPhoneViewController;
import com.qiyuan.gamesdk.core.ui.dialog.biz.BindPhoneViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.ChangeBindPhoneViewController2_1;
import com.qiyuan.gamesdk.core.ui.dialog.biz.ChangeBindPhoneViewController2_2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.HasRegisteredAndToLoginViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.HasRegisteredAndToSetPasswordViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.LoginViewController;
import com.qiyuan.gamesdk.core.ui.dialog.biz.LoginViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.RealNameAuthController;
import com.qiyuan.gamesdk.core.ui.dialog.biz.RealNameAuthController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.RegisterViewController;
import com.qiyuan.gamesdk.core.ui.dialog.biz.RegisterViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.ResetPasswordSuccessfulViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.ResetPasswordViewController;
import com.qiyuan.gamesdk.core.ui.dialog.biz.ResetPasswordViewController2;
import com.qiyuan.gamesdk.core.ui.dialog.biz.RetrievePasswordViewController2;
import com.qiyuan.gamesdk.core.ui.floatview.AnnouncementManager;
import com.qiyuan.gamesdk.core.ui.widget.ExitAlertDialogView;
import com.qiyuan.gamesdk.core.ui.widget.GameDownloadDialogView;
import com.qiyuan.gamesdk.core.ui.widget.TermsOfServiceAlertDialogView;
import com.qiyuan.gamesdk.core.ui.widget.TipsAlertDialogView;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.AnnouncementInfo;
import com.qiyuan.gamesdk.model.GameUpdateInfo;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;

import java.util.List;

/**
 * Created by Win on 15/6/10.
 */
public class ViewControllerNavigator {


    private static final String TAG = "QYSDK:ViewControllerNavigator ";

    private static ViewControllerNavigator _instance;

    private QYMainDialog mDialog;//do not access directly

    private ViewControllerNavigator() {

    }

    public static synchronized ViewControllerNavigator getInstance() {
        if (_instance == null) {
            _instance = new ViewControllerNavigator();
        }
        return _instance;
    }


    public void uninit() {
//        mDialog = null;
        _instance = null;
    }

    // ************ Interface ************** //

    /**
     * @param params
     * @return
     */
    public boolean toLogin(IDialogParam params) {
        Log.d(TAG, "toLogin: ");
        checkParam(params);
        List<AccountHistoryInfo> allGameAuthHistories
                = ApiFacade.getInstance().getAccountHistories();
        if (!allGameAuthHistories.isEmpty()) {
            AccountHistoryInfo historyInfo = allGameAuthHistories
                    .get(0);
            Log.d(TAG, "toLogin: historyInfo:" + historyInfo.toString());
            if (historyInfo.is_logout == 0) {
                loginAuto(params);
                return true;
            }
        }
        return loginPhone(params);
    }

    public void beforeLogin(final IDialogParam params) {
        ApiFacade.getInstance().requestAnnouncement2(1, new IOperateCallback<List<AnnouncementInfo>>() {
            @Override
            public void onResult(int i, List<AnnouncementInfo> announcementInfos) {
                if (announcementInfos != null && announcementInfos.size() > 0) {
                    for (AnnouncementInfo announcementInfo : announcementInfos) {
                        AnnouncementManager.getInstance().addAnnouncement(announcementInfo);
                    }
                    AnnouncementManager.getInstance().show((Activity) params.getActivityContext());
                    AnnouncementManager.getInstance().setAnnounceInterface(new AnnouncementManager.AnnounceInterface() {
                        @Override
                        public void afterUninit() {
                            toLogin(params);
                        }
                    });
                } else {
                    toLogin(params);
                }
            }
        });
    }

    public boolean loginPhone(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new LoginViewController(params.getActivityContext(), params));
    }

    public boolean loginVisitors(IDialogParam params) {
        Log.d(TAG, "loginVisitors: ");
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new LoginViewController(params.getActivityContext(), params));
    }

    public void loginAuto(IDialogParam params) {
        Log.d(TAG, "loginAuto: ");
        checkParam(params);
        new LoginViewController(params.getActivityContext(), params).loginAutoImpl();
    }

    public boolean toRegister(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new RegisterViewController(params.getActivityContext(), params));
    }

    /**
     * 2.0版本--注册
     */
    public boolean toRegister2(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new RegisterViewController2(params.getActivityContext(), params));
    }

    /**
     * 2.0版本--已经注册，并去登录
     */
    public boolean toHasRegisterAndToLogin2(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new HasRegisteredAndToLoginViewController2(params.getActivityContext(), params));
    }
    /**
     * 2.0版本--已经注册，并去设置密码
     */
    public boolean toHasRegisterAndToSetPassword2(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new HasRegisteredAndToSetPasswordViewController2(params.getActivityContext(), params));
    }

    /**
     * 2.0版本--找回密码
     */
    public boolean toRetrievePasswordViewController2(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new RetrievePasswordViewController2(params.getActivityContext(), params));
    }
    /**
     * 2.0版本--账户中心
     */
    public boolean toAccountCenter2() {
//        checkParam(params);
        return getDialog(CoreManager.getActivity())
                .show(new AccountCenterViewController2(CoreManager.getActivity(), null));
    }

    /**
     * 2.0版本--手机登录
     */
    public boolean tologinPhone2(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new LoginViewController2(params.getActivityContext(), params));
    }

    public boolean toRealNameAuth(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new RealNameAuthController(params.getActivityContext(), params));
    }
    /**
     * 2.0版本--实名认证
     */
    public boolean toRealNameAuth2() {
        return getDialog(CoreManager.getActivity())
                .show(new RealNameAuthController2(CoreManager.getActivity(), null));
    }

    public boolean toBindPhone(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new BindPhoneViewController(params.getActivityContext(), params));
    }

    /**
     * 2.0版本--绑定手机
     */
    public boolean toBindPhone2() {
        return getDialog(CoreManager.getActivity())
                .show(new BindPhoneViewController2(CoreManager.getActivity(), null));
    }

    /**
     * 2.0版本--绑定手机成功
     */
    public boolean toBindPhoneSuccessful2() {
        return getDialog(CoreManager.getActivity())
                .show(new BindPhoneSuccessfulViewController2(CoreManager.getActivity(), null));
    }

    /**
     * 2.0版本--更换绑定手机页面1
     */
    public boolean toChangeBindPhone2_1() {
        return getDialog(CoreManager.getActivity())
                .show(new ChangeBindPhoneViewController2_1(CoreManager.getActivity(), null));
    }

    /**
     * 2.0版本--更换绑定手机页面2
     */
    public boolean toChangeBindPhone2_2() {
        return getDialog(CoreManager.getActivity())
                .show(new ChangeBindPhoneViewController2_2(CoreManager.getActivity(), null));
    }

    public boolean toResetPassword(IDialogParam params) {
        checkParam(params);
        return getDialog(params.getActivityContext())
                .show(new ResetPasswordViewController(params.getActivityContext(), params));
    }

    /**
     * 2.0版本--修改密码
     */
    public boolean toResetPassword2() {
        return getDialog(CoreManager.getActivity())
                .show(new ResetPasswordViewController2(CoreManager.getActivity(), null));
    }

    /**
     * 2.0版本--修改密码成功
     */
    public boolean toResetPasswordSuccessful2() {
        return getDialog(CoreManager.getActivity())
                .show(new ResetPasswordSuccessfulViewController2(CoreManager.getActivity(), null));
    }

    public ExitAlertDialogView toExitAlertDialogView(Context context) {
        ExitAlertDialogView alertDialogView = new ExitAlertDialogView(context);
        getDialog(context).show(alertDialogView);
        return alertDialogView;
    }


    public TipsAlertDialogView toTipsAlertDialogView(Context context) {
        TipsAlertDialogView alertDialogView = new TipsAlertDialogView(context);
        getDialog(context).show(alertDialogView);
        return alertDialogView;
    }

    public TermsOfServiceAlertDialogView toTermsOfServiceAlertDialogView(Context context, IDialogParam params) {
        TermsOfServiceAlertDialogView alertDialogView = new TermsOfServiceAlertDialogView(context, params);
        getDialog(context).show(alertDialogView);
        return alertDialogView;
    }

    public GameDownloadDialogView toGameDownloadDialogView(Context context, GameUpdateInfo gameUpdateInfo) {
        GameDownloadDialogView alertDialogView = new GameDownloadDialogView(context, gameUpdateInfo);
        QYMainDialog dialog = getDialog(context);
        dialog.setCancelable(false);
        dialog.show(alertDialogView);

        return alertDialogView;
    }


    public void close() {
        Log.d(TAG, "close: ");
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
    // ************ Interface END ************** //

    @TargetApi(17)
    private QYMainDialog getDialog(Context context) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("context is not an activity !!");
        }
        if (mDialog == null) {
            Log.w(TAG, "mDialog is null");
        } else if (mDialog.getOwnerActivity() == null) {
            Log.w(TAG, "getOwnerActivity is null");
        } else if (mDialog.getOwnerActivity() != context) {
            Log.w(TAG, "dialog activity is not the showing one");
        } else if (Build.VERSION.SDK_INT >= 17 && mDialog.getOwnerActivity().isDestroyed()) {
            Log.w(TAG, "ownerActivity is Destroyed");
        }
        if (mDialog == null || mDialog.getOwnerActivity() == null || mDialog.getOwnerActivity() != context) {
            mDialog = new QYMainDialog(context);
            mDialog.setOwnerActivity((Activity) context);
        } else if (Build.VERSION.SDK_INT >= 17 && mDialog.getOwnerActivity().isDestroyed()) {
            mDialog = new QYMainDialog(context);
        }
        return mDialog;
    }

    private void checkParam(IDialogParam params) {
        if (params == null || params.getActivityContext() == null) {
            throw new IllegalArgumentException(ResourceHelper.getString(R.string.exception_param_error));
        }
    }
}
