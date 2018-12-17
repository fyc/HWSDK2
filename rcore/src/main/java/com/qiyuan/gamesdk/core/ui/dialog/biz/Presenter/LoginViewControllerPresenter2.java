package com.qiyuan.gamesdk.core.ui.dialog.biz.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.qiyuan.gamesdk.PluginManager;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.LoginViewController2;
import com.qiyuan.gamesdk.core.ui.widget.DataPicker;
import com.qiyuan.gamesdk.core.ui.widget.DrawableEditText;
import com.qiyuan.gamesdk.core.ui.widget.TipsAlertDialogView;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.util.PopUtil;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qiyuan.gamesdk.util.ViewUtils;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginViewControllerPresenter2 {
    LoginViewController2 loginViewController2;

    public LoginViewControllerPresenter2(LoginViewController2 controller) {
        loginViewController2 = controller;
    }

    public void addTextWatcher(EditText... editTexts) {
        for (final EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    updateLoginButtonState();
                }
            });
        }
    }

    private void updateLoginButtonState() {
        if (loginViewController2.currentState == loginViewController2.STATE_LOGIN_PHONE) {
            if (loginViewController2.phoneEdit.length() == 0 || loginViewController2.phonePasswordEdit.length() == 0) {
                loginViewController2.loginButton.setEnabled(false);
            } else {
                loginViewController2.loginButton.setEnabled(true);
            }
        } else if (loginViewController2.currentState == loginViewController2.STATE_LOGIN_ACCOUNT) {
            if (loginViewController2.accountEdit.length() == 0 || loginViewController2.accountPasswordEdit.length() == 0) {
                loginViewController2.loginButton.setEnabled(false);
            } else {
                loginViewController2.loginButton.setEnabled(true);
            }
        }
    }


    public boolean checkPhoneInput(String phone, String password) {
        if (phone.length() != 11) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_valid_11_phone_num));
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_password_hint));
            return false;
        }
        putLoginPhoneInfo(phone, password);
        return true;
    }

    public boolean checkAccountInput(String account, String password) {
        if (account.length() < 4 || account.length() > 16) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_account_hint));
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            ToastUtils.showMsg(ResourceHelper.getString(R.string.register_password_hint));
            return false;
        }
        putLoginAccountInfo(account, password);
        return true;
    }

    private void putLoginPhoneInfo(String phone, String md5Pwd) {
        loginViewController2.requestingPhoneNumber = phone;
        loginViewController2.requestingPhoneMd5Pwd = md5Pwd;
    }

    private void putLoginAccountInfo(String account, String md5Pwd) {
        loginViewController2.requestingAccount = account;
        loginViewController2.requestingAccountMd5Pwd = md5Pwd;
    }

    public void loginByPhoneImpl(final String phone, final String password) {
        loginViewController2.showLoading();
        QyRespListener<AuthModel> callback = new QyRespListener<AuthModel>() {
            @Override
            public void onNetSucc(String url, Map params, AuthModel result) {
                if (params != null) {
                    Log.d(loginViewController2.TAG, "registerByPhone " + result.toString());
                    loginViewController2.notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
                    loginViewController2.close();
                } else {
                    Log.d(loginViewController2.TAG, "registerByPhone return null;");
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                loginViewController2.hideLoading();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                loginViewController2.hideLoading();
            }
        };

        ApiFacade.getInstance().loginByPhone(phone, password, callback);

    }

    public void loginByAccountImpl(final String account, final String password) {
        loginViewController2.showLoading();
        ApiFacade.getInstance().loginByUserName(password, account, new QyRespListener<AuthModel>() {
            @Override
            public void onNetworkComplete() {
                super.onNetworkComplete();
                loginViewController2.hideLoading();
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, AuthModel result) {
                if (params != null) {
                    Log.d(loginViewController2.TAG, "registerByPhone " + result.toString());
                    loginViewController2.notifyAuthResult(StatusCodeDef.SUCCESS, "", result);
                    loginViewController2.close();
                } else {
                    Log.d(loginViewController2.TAG, "registerByPhone return null;");
                }
            }
        });
    }


//    private void loginImpl(String account, String password) {
//        if (StringUtils.isBlank(account)) {
//            ToastUtils.showMsg(ResourceHelper.getString(R.string.phone_blank));
//            return;
//        }
//        if (account.length() != 11) {
//            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_11_phone_number));
//            return;
//        }
//        if (!account.startsWith("1") && !account.startsWith("9")) {
//            ToastUtils.showMsg(ResourceHelper.getString(R.string.please_input_valid_number));
//            return;
//        }
//        loginButton.setEnabled(false);
//        showLoading();
//        ApiFacade.getInstance().login2(account, code, new QyRespListener<LoginBean>() {
//            @Override
//            public void onNetworkComplete() {
//                loginButton.setEnabled(true);
//            }
//
//            @Override
//            public void onNetSucc(String url, Map<String, String> params, LoginBean result) {
////                onLoginSuccess(result);
//                hideLoading();
//                notifyAuthResult2(StatusCodeDef.SUCCESS, "", result);
//                PopUtil.get((Activity) context).showNoButton(result.getData().getMobile_phone() + "，");
//                close();
//            }
//
//            @Override
//            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
//                super.onNetError(url, params, errno, errmsg);
//                onLoginFail();
//                loginButton.setEnabled(true);
//            }
//
//            @Override
//            public void onFail(int errorNo, String errmsg) {
//                super.onFail(errorNo, errmsg);
//                onLoginFail();
//                loginButton.setEnabled(true);
//            }
//        });
//    }


    public void loginVisitorsWrapImpl() {
        List<AccountHistoryInfo> allGameAuthHistories
                = ApiFacade.getInstance().getAccountHistories();
        if (!allGameAuthHistories.isEmpty()) {
            AccountHistoryInfo historyInfo = allGameAuthHistories
                    .get(0);
            if (!TextUtils.isEmpty(historyInfo.guest) && historyInfo.guest.equals("false")) {
                loginVisitorsImpl();//进入游客登录流程
            } else {
                loginAutoImpl();//进入自动登录流程
            }
        } else {
            loginVisitorsImpl();//进入游客登录流程
        }
    }

    public void loginVisitorsImpl() {
        loginViewController2.showLoading();
        ApiFacade.getInstance().loginVisitors(new QyRespListener<LoginBean>() {
            @Override
            public void onNetworkComplete() {
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, LoginBean result) {
                loginViewController2.hideLoading();
                loginViewController2.notifyAuthResult2(StatusCodeDef.SUCCESS, "", result);
                PopUtil.get((Activity) loginViewController2.context).showNoButton(result.getData().getUser_id() + "，");
                loginViewController2.close();
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                loginViewController2.onLoginFail();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                loginViewController2.onLoginFail();
            }
        });
    }

    public void loginAutoImpl() {
        loginViewController2.showLoading();
        ApiFacade.getInstance().loginAuto(new QyRespListener<LoginBean>() {
            @Override
            public void onNetworkComplete() {
            }

            @Override
            public void onNetSucc(String url, Map<String, String> params, final LoginBean result) {
                loginViewController2.hideLoading();
                loginViewController2.notifyAuthResult2(StatusCodeDef.SUCCESS, "", result);
                if (!TextUtils.isEmpty(result.getData().getGuest()) && result.getData().getGuest().equals("true")) { //游客，进入手机绑定
                    final PopUtil popUtil = PopUtil.get((Activity) loginViewController2.context);
                    popUtil.showHasButton("游客：" + result.getData().getUser_id() + "，",
                            new PopUtil.PopOnListener() {
                                @Override
                                public void onClick(View v) {
                                    ApiFacade.getInstance().logout(PluginManager.getInstance().getLogoutCallback());
                                    popUtil.dismiss();
                                }

                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    ViewControllerNavigator.getInstance().toTipsAlertDialogView(loginViewController2.context)
                                            .setOnclick(new TipsAlertDialogView.Onclick() {
                                                @Override
                                                public void onPositive() {
                                                    ViewControllerNavigator.getInstance().toBindPhone(loginViewController2.getDialogParam());
                                                }

                                                @Override
                                                public void onNegative() {
                                                }
                                            });
                                }
                            });
                } else if (!TextUtils.isEmpty(result.getData().getGuest()) && result.getData().getGuest().equals("false")) {//手机用户，进入实名认证
                    final PopUtil popUtil = PopUtil.get((Activity) loginViewController2.context);
                    popUtil.showHasButton(result.getData().getMobile_phone() + "，",
                            new PopUtil.PopOnListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(loginViewController2.TAG, "切换账号，假装成功退出=1=" + ApiFacade.getInstance().getMainUid());
                                    ApiFacade.getInstance().logout(PluginManager.getInstance().getLogoutCallback());
                                    popUtil.dismiss();
                                }

                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    if (result.getData().getNeed_real().equals("1")) {
                                        ViewControllerNavigator.getInstance().toRealNameAuth(loginViewController2.getDialogParam());
                                    }
                                }
                            });
                }
                loginViewController2.close();
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
                loginViewController2.onLoginFail();
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
                loginViewController2.onLoginFail();
            }
        });
    }

    public HistoryPickerController newHistoryPickerController(View anchorView) {
        return new HistoryPickerController(anchorView);
    }
    public AccountEditViewController newAccountEditViewController(DrawableEditText accountEditView) {
        return new AccountEditViewController(accountEditView);
    }
    public class HistoryPickerController {

        DataPicker picker = null;
        View anchor;
        DataPicker.DataPickerListener<AccountHistoryWrapper> listener = null;

        public HistoryPickerController(View anchorView) {
            anchor = anchorView;
        }

        public void tryDismissPicker() {
            if (picker != null) {
                picker.dismiss();
                picker.setDataPickerListener(null);
                picker = null;
            }
        }

        public void tryShowPicker() {
            tryDismissPicker();
            tryListAccountHistories();
        }

        private void tryListAccountHistories() {

            List<AccountHistoryInfo> allGameAuthHistories
                    = ApiFacade.getInstance().getAccountHistories();
            Log.d(loginViewController2.TAG, "tryListAccountHistories: " + allGameAuthHistories.size());
            if (allGameAuthHistories.isEmpty()) {
                return;
            }
            showNewDataPicker(toAccountHistoryWrapper(allGameAuthHistories));
        }


        private void showNewDataPicker(List<AccountHistoryWrapper> data) {
            int height = (int) (ViewUtils.distanceToScreenBottom(anchor) * 0.6f);
            int width = (int) (anchor.getWidth());
//            int dx = (int) ((anchor.getWidth() - width) / 2.0f);
            picker = new DataPicker<AccountHistoryWrapper>(loginViewController2.getContext(), width, height, true);
            picker.setDataSource(data);
            picker.setDataPickerListener(listener);
            picker.showAsDropDown(loginViewController2.accountEdit, 0, 0);
        }

        private List<AccountHistoryWrapper> toAccountHistoryWrapper(
                @NonNull List<AccountHistoryInfo> authHistories) {
            List<AccountHistoryWrapper> wrapperList
                    = new ArrayList<>(authHistories.size());
            for (AccountHistoryInfo info : authHistories) {
                wrapperList.add(new AccountHistoryWrapper(info));
            }
            return wrapperList;
        }

        public void setDataPickListener(DataPicker.DataPickerListener<AccountHistoryWrapper> l) {
            listener = l;
        }
    }

    public static class AccountHistoryWrapper implements DataPicker.IDataWrapper {
//        public static final int DATA_TYPE_ACCOUNT = 0;
//        public static final int DATA_TYPE_PHONE = 1;

        private AccountHistoryInfo orgInfo;
//        private int dataType = DATA_TYPE_ACCOUNT;

        public AccountHistoryWrapper(@NonNull AccountHistoryInfo orgInfo) {
            this.orgInfo = orgInfo;
//            this.dataType = dataType;
        }

        @Override
        public String dataName() {

            return orgInfo.phone;
        }

        @Override
        public String key() {
            return String.valueOf(orgInfo.userID);
        }

        public AccountHistoryInfo data() {
            return orgInfo;
        }

    }
    public class AccountEditViewController {
        DrawableEditText editView;
        int rightDrawableStateCount = 0;
        boolean noRightDrawable;

        public AccountEditViewController(DrawableEditText accountEditView) {
            editView = accountEditView;
        }

        public void noRightDrawableSate() {
            noRightDrawable = true;
            rightDrawableStateCount = 0;
            editView.setDrawableRight(0);
        }

        public void resetRightDrawableState() {
            noRightDrawable = false;
            rightDrawableStateCount = 0;
            editView.setDrawableRight(R.drawable.qy_sdk_icon_expand);
        }

        public int nextRightDrawableState() {
            int state = -1;
            if (noRightDrawable) {
                return state;
            }
            state = ++rightDrawableStateCount % 2;
            if (state == 0) {
                editView.setDrawableRight(R.drawable.qy_sdk_icon_expand);
            } else {
                editView.setDrawableRight(R.drawable.qy_sdk_icon_collapse);
            }
            return state;
        }
    }
}
