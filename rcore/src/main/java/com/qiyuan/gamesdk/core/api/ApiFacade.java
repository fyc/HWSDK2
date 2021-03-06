package com.qiyuan.gamesdk.core.api;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.def.IAnnouncementApi;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.api.def.IChildrenAccountHistoryApi;
import com.qiyuan.gamesdk.core.api.def.IPaymentApi;
import com.qiyuan.gamesdk.core.api.def.ISecurityApi;
import com.qiyuan.gamesdk.core.api.def.IUpgradeApi;
import com.qiyuan.gamesdk.core.api.impl.ApiLoader;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.FileDownListener;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.AnnouncementInfo;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.model.BalanceInfo;
import com.qiyuan.gamesdk.model.ChildrenAccountHistoryInfo;
import com.qiyuan.gamesdk.model.CouponCountInfo;
import com.qiyuan.gamesdk.model.CouponInfo;
import com.qiyuan.gamesdk.model.GameDiscountInfo;
import com.qiyuan.gamesdk.model.GamePackages;
import com.qiyuan.gamesdk.model.GameUpdateInfo;
import com.qiyuan.gamesdk.model.GetCouponInfo;
import com.qiyuan.gamesdk.model.InventoriesInfo;
import com.qiyuan.gamesdk.model.PatchUpdateBean;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.model.PaymentInfo;
import com.qygame.qysdk.outer.util.Log;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.def.IAccountHistoryApi;
import com.qiyuan.gamesdk.core.api.def.IAnnouncementApi;
import com.qiyuan.gamesdk.core.api.def.IApiWrapping;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.api.def.IChannelApi;
import com.qiyuan.gamesdk.core.api.def.IChildrenAccountHistoryApi;
import com.qiyuan.gamesdk.core.api.def.IPaymentApi;
import com.qiyuan.gamesdk.core.api.def.IReportApi;
import com.qiyuan.gamesdk.core.api.def.ISecurityApi;
import com.qiyuan.gamesdk.core.api.def.IUpgradeApi;
import com.qiyuan.gamesdk.core.api.impl.ApiLoader;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.FileDownListener;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.AccountHistoryInfo;
import com.qiyuan.gamesdk.model.AnnouncementInfo;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.model.BalanceInfo;
import com.qiyuan.gamesdk.model.ChildrenAccountHistoryInfo;
import com.qiyuan.gamesdk.model.CouponCountInfo;
import com.qiyuan.gamesdk.model.CouponInfo;
import com.qiyuan.gamesdk.model.GameDiscountInfo;
import com.qiyuan.gamesdk.model.GamePackages;
import com.qiyuan.gamesdk.model.GameUpdateInfo;
import com.qiyuan.gamesdk.model.GetCouponInfo;
import com.qiyuan.gamesdk.model.InventoriesInfo;
import com.qiyuan.gamesdk.model.PatchUpdateBean;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by levyyoung on 15/6/8.
 */
//接口继承关系隐藏到IApiFacade
public class ApiFacade implements IApiFacade {


    private static final String TAG = "QYSDK:APIFacade ";
    private static ApiFacade ourInstance = null;

    /**
     * API实例尽量使用lazy-load;
     * 重要模块可以在preload方法进行预加载
     *
     * @see IApiWrapping getApiInstance
     */
    private ApiFacade() {

    }

    public static synchronized ApiFacade getInstance() {
        if (ourInstance == null) {
            ourInstance = new ApiFacade();
        }
        return ourInstance;
    }


    /**
     * ===========================================================================================
     * ===================================加载次序及方式 BEGIN =====================================
     * ===========================================================================================
     */

    /**
     * 预加载
     * 某些优先级高的模块可以放这里进行预加载
     *
     * @see static void ApiLoader.preload()
     */
    public static void preload() {
        ApiLoader.preload();
    }

    //下划线开头命名，减少误访问可能。调用API实例请使用Getters
    private ISecurityApi _securityApi;
    private IReportApi _reportApi;
    private IUpgradeApi _upgradeApi;
    private IAuthApi _authApi;
    private IAccountHistoryApi _accountHistoryApi;
    private IChildrenAccountHistoryApi _childrenAccountHistoryApi;
    private IPaymentApi _paymentApi;
    private IChannelApi _channelApi;
    private IAnnouncementApi _announcementApi;

    synchronized ISecurityApi securityApi() {
        if (_securityApi == null) {
            _securityApi = (ISecurityApi) ApiLoader.getApiInstance(ISecurityApi.class);
        }
        return _securityApi;
    }

    synchronized IReportApi reportApi() {
        if (_reportApi == null) {
            _reportApi = (IReportApi) ApiLoader.getApiInstance(IReportApi.class);
        }
        return _reportApi;
    }

    synchronized IUpgradeApi upgradeApi() {
        if (_upgradeApi == null) {
            _upgradeApi = (IUpgradeApi) ApiLoader.getApiInstance(IUpgradeApi.class);
        }
        return _upgradeApi;
    }

    synchronized IAuthApi authApi() {
        if (_authApi == null) {
            _authApi = (IAuthApi) ApiLoader.getApiInstance(IAuthApi.class);
        }
        return _authApi;
    }

    synchronized IChildrenAccountHistoryApi childrenAccountHistoryApi() {
        if (_childrenAccountHistoryApi == null) {
            _childrenAccountHistoryApi = (IChildrenAccountHistoryApi) ApiLoader.getApiInstance(IChildrenAccountHistoryApi.class);
        }
        return _childrenAccountHistoryApi;
    }

    synchronized IAnnouncementApi announcementApi() {
        if (_announcementApi == null) {
            _announcementApi = (IAnnouncementApi) ApiLoader.getApiInstance(IAnnouncementApi.class);
        }
        return _announcementApi;
    }

    synchronized IAccountHistoryApi accountHistoryApi() {
        if (_accountHistoryApi == null) {
            _accountHistoryApi = (IAccountHistoryApi) ApiLoader.getApiInstance(IAccountHistoryApi.class);
        }
        return _accountHistoryApi;
    }

    synchronized IPaymentApi paymentApi() {
        if (_paymentApi == null) {
            _paymentApi = (IPaymentApi) ApiLoader.getApiInstance(IPaymentApi.class);
        }
        return _paymentApi;
    }

    synchronized IChannelApi channelApi() {
        if (_channelApi == null) {
            _channelApi = (IChannelApi) ApiLoader.getApiInstance(IChannelApi.class);
        }
        return _channelApi;
    }

    /**
     * ===========================================================================================
     * =================================== 加载次序及方式 END  =====================================
     * ===========================================================================================
     */

    // **************************** Auth Begin ************************************ //
    @Override
    public void registerByUserName(String password, String userName, QyRespListener<AuthModel> callback) {
        authApi().registerByUserName(password, userName, callback);
    }

    @Override
    public void registerByPhone(String phone, String password, String verificationCode, QyRespListener<AuthModel> callback) {
        authApi().registerByPhone(phone, password, verificationCode, callback);
    }

    @Override
    public void registerChildAccount(String childUserName, QyRespListener<AuthModel.childAccount> callback) {
        authApi().registerChildAccount(childUserName, callback);
    }

    @Override
    public void requestVerificationCode(String phone, int type, QyRespListener<Void> callback) {
        authApi().requestVerificationCode(phone, type, callback);
    }

    @Override
    public void requestVerificationCode(String phone, int type, int retry, QyRespListener<Void> callback) {
        authApi().requestVerificationCode(phone, type, retry, callback);
    }

    @Override
    public void requestVerificationCode2(String phone, int type, int retry, QyRespListener<String> callback) {
        authApi().requestVerificationCode2(phone, type, retry, callback);
    }

    @Override
    public void login(String account, String pwd, QyRespListener<AuthModel> callback) {
        authApi().login(account, pwd, callback);
    }

    @Override
    public void login2(String account, String pwd, QyRespListener<LoginBean> callback) {
        authApi().login2(account, pwd, callback);
    }

    @Override
    public void loginVisitors(QyRespListener<LoginBean> callback) {
        authApi().loginVisitors(callback);
    }

    @Override
    public void loginAuto(QyRespListener<LoginBean> callback) {
        authApi().loginAuto(callback);
    }

    @Override
    public void logout(IOperateCallback<String> iOperateCallback) {
        authApi().logout(iOperateCallback);
    }

    @Override
    public void logout() {
        authApi().logout();
    }

    @Override
    public void getBalance(QyRespListener<BalanceInfo> callback) {
        authApi().getBalance(callback);
    }

    @Override
    public void getCouponInfos(String type, QyRespListener<CouponInfo> callback) {
        authApi().getCouponInfos(type, callback);
    }

    @Override
    public String getSession() {
        return authApi().getSession();
    }

    @Override
    public long getMainUid() {
        return authApi().getMainUid();
    }

    @Override
    public long getSubUid() {
        return authApi().getSubUid();
    }

    @Override
    public String getUserName() {
        return authApi().getUserName();
    }

    @Override
    public String getSubUserName() {
        return authApi().getSubUserName();
    }

    @Override
    public String getPhone() {
        return authApi().getPhone();
    }

    /**
     * @return 是否登录
     */
    @Override
    public boolean isLogin() {
        return authApi().isLogin();
    }

    @Override
    public void getCouponCount(String type, QyRespListener<CouponCountInfo> callback) {
        authApi().getCouponCount(type, callback);
    }

    @Override
    public void childAccountEvent() {
        authApi().childAccountEvent();
    }

    @Override
    public void getCouponCenter(QyRespListener<CouponInfo> callback) {
        authApi().getCouponCenter(callback);
    }

    @Override
    public void getInventories(QyRespListener<InventoriesInfo> callback) {
        authApi().getInventories(callback);
    }

    @Override
    public void getGamePackage(QyRespListener<GamePackages> callback) {
        authApi().getGamePackage(callback);
    }

    @Override
    public void receiveGamePackage(String packageId, QyRespListener<GamePackages.GamePackageInfo> callback) {
        authApi().receiveGamePackage(packageId, callback);
    }

    @Override
    public void getGamePackageDetail(String packageId, QyRespListener<GamePackages.GamePackageInfo> callback) {
        authApi().getGamePackageDetail(packageId, callback);
    }

    @Override
    public void requestGameDiscount(QyRespListener<GameDiscountInfo> callback) {
        authApi().requestGameDiscount(callback);
    }

    @Override
    public void getCoupon(int actId, QyRespListener<GetCouponInfo> callback) {
        authApi().getCoupon(actId, callback);
    }

    @Override
    public void getCouponByRule(int ruleId, QyRespListener<GetCouponInfo> callback) {
        authApi().getCouponByRule(ruleId, callback);
    }

    @Override
    public AuthModel getAuth() {
        return authApi().getAuth();
    }

    // **************************** Auth End ************************************ //


    /**
     * =============================================================================================
     * =================================  ChildrenAccountHistoryApi BEGIN  ==========================
     * =============================================================================================
     */
    /**
     * 获取登录历史记录
     *
     * @return 改设备上所有使用SDK登录过的账号记录
     */
    @Override
    public List<ChildrenAccountHistoryInfo> getAllChildrenAccountHistories() {
        return childrenAccountHistoryApi().getAllChildrenAccountHistories();
    }

    /**
     * 删除登录历史
     *
     * @param childrenUserID 用户小号数字账号
     */
    @Override
    public void deleteChildrenAccountHistory(String childrenUserID) {
        childrenAccountHistoryApi().deleteChildrenAccountHistory(childrenUserID);
    }

    /**
     * 更新登录历史
     *
     * @param history 登录历史内容
     */
    @Override
    public void insertOrUpdateChildrenAccountHistory(ChildrenAccountHistoryInfo history) {
        childrenAccountHistoryApi().insertOrUpdateChildrenAccountHistory(history);
    }

    /**
     * 获取知道账号历史信息
     *
     * @param account 用户数字账号
     * @return AuthHistoryInfo 账号历史信息
     * @see
     */
    @Override
    public ChildrenAccountHistoryInfo getChildrenAccountHistory(String account) {
        return childrenAccountHistoryApi().getChildrenAccountHistory(account);
    }

    /**
     * 获取当前游戏登录历史记录
     *
     * @return 当前游戏登录历史记录
     */
    @Override
    public List<ChildrenAccountHistoryInfo> getChildrenAccountHistory(String userId, String gameId) {
        return childrenAccountHistoryApi().getChildrenAccountHistory(userId, gameId);
    }

    @Override
    public List<ChildrenAccountHistoryInfo> getCurrentChildrenAccountHistory() {
        return childrenAccountHistoryApi().getCurrentChildrenAccountHistory();
    }

    @Override
    public void updateCurrentChildrenAccount(List<ChildrenAccountHistoryInfo> accountHistoryInfoList) {
        childrenAccountHistoryApi().updateCurrentChildrenAccount(accountHistoryInfoList);
    }

    @Override
    public void editChildrenAccountName(long childUserId, String childUserName, QyRespListener callback) {
        childrenAccountHistoryApi().editChildrenAccountName(childUserId, childUserName, callback);
    }

    /**
     * =============================================================================================
     * ===================================  AuthHistory END  =======================================
     * =============================================================================================
     */

    /**
     * =============================================================================================
     * ===================================SecurityApi Begin=========================================
     * =============================================================================================
     */
    @Override
    public void modifyPayPassword(String oldPwd, String newPwd, QyRespListener callback) {
        securityApi().modifyPayPassword(oldPwd, newPwd, callback);
    }

    @Override
    public void modifyPassword(String oldPwd, String newPwd, QyRespListener callback) {
        securityApi().modifyPassword(oldPwd, newPwd, callback);
    }

    @Override
    public void forgetPayPassword(String mobile, String newPwd, String vcode, QyRespListener callback) {
        securityApi().forgetPayPassword(mobile, newPwd, vcode, callback);
    }

    @Override
    public void forgetPassword(String mobile, String newPwd, String vcode, QyRespListener callback) {
        securityApi().forgetPassword(mobile, newPwd, vcode, callback);
    }

    @Override
    public void bindPhone(String phoneNum, String smsVCode, QyRespListener callback) {
        securityApi().bindPhone(phoneNum, smsVCode, callback);
    }

    @Override
    public void bindPhone2(String phoneNum, String smsVCode, QyRespListener callback) {
        securityApi().bindPhone2(phoneNum, smsVCode, callback);
    }

    @Override
    public void unbindPhone(String phoneNum, String smsVCode, QyRespListener callback) {
        securityApi().unbindPhone(phoneNum, smsVCode, callback);
    }

    @Override
    public void realNameAuth(String real_name, String card_no, QyRespListener callback) {
        securityApi().realNameAuth(real_name, card_no, callback);
    }

    @Override
    public void setPayPassword(String password, QyRespListener callback) {
        securityApi().setPayPassword(password, callback);
    }

    @Override
    public void verifyPayPassword(String payPassword, QyRespListener callback) {
        securityApi().verifyPayPassword(payPassword, callback);
    }

    /**
     * =============================================================================================
     * ===================================SecurityApi End  =========================================
     * =============================================================================================
     */

    /**
     * 检查游戏更新
     *
     * @param cpId             游戏厂商id
     * @param gameId           游戏id
     * @param iOperateCallback 调用方回调。成功返回GameUpdateInfo ，失败返回错误码和错误信息
     */

    @Override
    public void upgradeRequest(String cpId, String gameId, String versionName, String versionCode,
                               QyRespListener<GameUpdateInfo> iOperateCallback) {
        upgradeApi().upgradeRequest(cpId, gameId, versionName, versionCode, iOperateCallback);
    }

    @Override
    public void updateCheck(@NonNull Map<String, String> params, QyRespListener<PatchUpdateBean> listener) {
        upgradeApi().updateCheck(params, listener);
    }

    @Override
    public void downLoadFile(String url, File saveFilePath, String fileName, String md5, FileDownListener listener) {
        upgradeApi().downLoadFile(url, saveFilePath, fileName, md5, listener);
    }


    /**
     * =============================================================================================
     * ===================================reportApi start  =========================================
     * =============================================================================================
     */


    @Override
    public void feedback(String content, QyRespListener<Void> callback) {
        reportApi().feedback(content, callback);
    }

    @Override
    public void reportActivate(Activity activity, IOperateCallback<QyDataBean> callback) {
        reportApi().reportActivate(activity, callback);
    }

    @Override
    public void onLineEvent(IOperateCallback<Void> callback) {
        reportApi().onLineEvent(callback);
    }

    @Override
    public void onCharacterEvent(Map<String, String> params, IOperateCallback<Void> callback) {
        reportApi().onCharacterEvent(params, callback);
    }

    /**
     * =============================================================================================
     * ===================================reportApi end  ===========================================
     * =============================================================================================
     */


    /**
     * =============================================================================================
     * ===================================AccountHistoryApi Begin===================================
     * =============================================================================================
     */
    /**
     * 获取账号历史记录
     *
     * @param uid uid
     * @return 账号记录 or null
     */
    @Override
    public AccountHistoryInfo getAccountHistoryByUid(String uid) {
        return accountHistoryApi().getAccountHistoryByUid(uid);
    }

    @Override
    public AccountHistoryInfo getCurrentHistoryAccount() {
        return accountHistoryApi().getCurrentHistoryAccount();
    }

    @Override
    public void insertOrUpdateAccountHistory(@NonNull AccountHistoryInfo accountHistoryInfo) {
        accountHistoryApi().insertOrUpdateAccountHistory(accountHistoryInfo);
    }

    /**
     * @return 账号记录列表
     */
    @Override
    public List<AccountHistoryInfo> getAccountHistories() {
        return accountHistoryApi().getAccountHistories();
    }

    /**
     * 插入或更新账号记录
     *
     * @param accountHistoryCV 账号记录CV. 必须包含account字段
     */
    @Override
    public void insertOrUpdateAccountHistory(@NonNull ContentValues accountHistoryCV) {
        accountHistoryApi().insertOrUpdateAccountHistory(accountHistoryCV);
    }

    /**
     * 根据账号删除记录
     *
     * @param account 账号
     */
    @Override
    public void deleteAccountHistory(String account) {
        accountHistoryApi().deleteAccountHistory(account);
    }

    @Override
    public void refresAccounthHistory() {
        accountHistoryApi().refresAccounthHistory();
    }

    /**
     * 从历史记录获取登录密码
     *
     * @param account 账号
     * @return md5密码
     */
    @Override
    public String getPasswordFromHistory(String account) {
        return accountHistoryApi().getPasswordFromHistory(account);
    }

//    /**
//     * 修改历史记录中的密码
//     *
//     * @param passwd
//     */
//    @Override
//    public void resetPasswd(String passwd) {
//        accountHistoryApi().resetPasswd(passwd);
//    }


    @Override
    public String getPasswordFromHistoryByPhone(String phone) {
        return accountHistoryApi().getPasswordFromHistoryByPhone(phone);
    }

    @Override
    public List<AccountHistoryInfo> getCurrentGameAuthHistories() {
        return accountHistoryApi().getCurrentGameAuthHistories();
    }

    @Override
    public String getPasswordFromHistoryByUsername(String username) {
        return accountHistoryApi().getPasswordFromHistoryByUsername(username);
    }

    @Override
    public AccountHistoryInfo getAccountHistoryByUserName(String username) {
        return accountHistoryApi().getAccountHistoryByUserName(username);
    }

    @Override
    public AccountHistoryInfo getHistoryAccountByPhone(String phone) {
        return accountHistoryApi().getHistoryAccountByPhone(phone);
    }

    /**
     * =============================================================================================
     * ===================================AccountHistoryApi End=====================================
     * =============================================================================================
     */


    /**
     * =============================================================================================
     * ========================================PaymentApi Start=====================================
     * =============================================================================================
     */
    @Override
    public void order(PaymentInfo paymentInfo, Activity startUpActivity, IOperateCallback<String> orderCallback) {
        paymentApi().order(paymentInfo, startUpActivity, orderCallback);
    }

    @Override
    public void orderH5(@Nullable Activity activity, Long cliBuyerId, String cliSellerId, String cpOrderNo, String cpOrderTitle, float cpPrice) {
        paymentApi().orderH5(activity, cliBuyerId, cliSellerId, cpOrderNo,cpOrderTitle,cpPrice);
    }

    @Override
    public void notifyOrderState(int payState, String orderInfo) {
        paymentApi().notifyOrderState(payState, orderInfo);
    }

    @Override
    public void orderThroughClient(int payWay, String dataJsonStr) {
        paymentApi().orderThroughClient(payWay, dataJsonStr);
    }

    @Override
    public void orderThroughClient(int payWay, String dataJsonStr, boolean isFromApp) {
        paymentApi().orderThroughClient(payWay, dataJsonStr, isFromApp);
    }

    @Override
    public void updatePaymentActivity(Activity activity) {
        paymentApi().updatePaymentActivity(activity);
    }

    @Override
    public void closeNotify() {
        paymentApi().closeNotify();
    }

    @Override
    public void getOrderFromApp(Map<String, String> params) {
        paymentApi().getOrderFromApp(params);
    }

    @Override
    public void orderPay(Map<String, String> params, QyRespListener<String> callback) {
        paymentApi().orderPay(params, callback);
    }

    @Override
    public void clearOrderCache() {
        paymentApi().clearOrderCache();
    }

    /**
     * =============================================================================================
     * =========================================PaymentApi End======================================
     * =============================================================================================
     */

    /**
     * =============================================================================================
     * =========================================Channel Api Begin===================================
     * =============================================================================================
     */

    /**
     * 从配置或安装包读取渠道信息
     */
    @Override
    public void setupChannelInfo() {
        channelApi().setupChannelInfo();
    }

    /**
     * 设置当前游戏Id.
     * 游戏ID由CP传入，initCoreManager时设置。
     *
     * @param gameID 游戏Id
     */
    @Override
    public void setCurrentGameID(String gameID) {
        channelApi().setCurrentGameID(gameID);
    }

    /**
     * 获取
     *
     * @return 当前游戏ID
     */
    @Override
    public String getCurrentGameID() {
        return channelApi().getCurrentGameID();
    }

    /**
     * 获取
     * 从当前游戏软件信息中获取
     *
     * @return 当前游戏名称
     */
    @Override
    public String getCurrentGameName() {
        return channelApi().getCurrentGameName();
    }


    @Override
    public String getChannel() {
        return channelApi().getChannel();
    }

    @Override
    public String getSdkKey() {
        return channelApi().getSdkKey();
    }

    @Override
    public void setSdkKey(String key) {
        channelApi().setSdkKey(key);
    }


    /**
     * =============================================================================================
     * =========================================Channel Api End=====================================
     * =============================================================================================
     */

    /**
     * =============================================================================================
     * =========================================Announcement Api Begin===================================
     * =============================================================================================
     */

    @Override
    public void requestAnnouncement(int from, IOperateCallback<List<AnnouncementInfo>> callback) {
        announcementApi().requestAnnouncement(from, callback);
    }

    @Override
    public void requestAnnouncement2(int from, IOperateCallback<List<AnnouncementInfo>> callback) {
        announcementApi().requestAnnouncement2(from, callback);
    }

    @Override
    public List<AnnouncementInfo> getLocalAnnouncement() {
        return announcementApi().getLocalAnnouncement();
    }

    /**
     * =============================================================================================
     * =========================================Announcement Api Begin===================================
     * =============================================================================================
     */
    public void setLastLoginChildAccount(AuthModel.childAccount lastLoginChildAccount) {
        if (lastLoginChildAccount != null) {
            SharedPreferences preferences = CoreManager.getContext().getSharedPreferences(getMainUid() + "", Context.MODE_PRIVATE);
            preferences.edit().putLong("qysdk_childUserID", lastLoginChildAccount.getChildUserID()).apply();
            preferences.edit().putString("qysdk_childUserName", lastLoginChildAccount.getChildUserName()).apply();
            preferences.edit().putString("qysdk_QYAccount", lastLoginChildAccount.getQYAccount()).apply();
            Log.d(TAG, "setLastLoginChildAccount: " + lastLoginChildAccount.toString());
        }
    }

    public void setLastLoginChildAccount(long childUserId, String childUserName, String childQYAcount) {
        if (childUserId != 0) {
            SharedPreferences preferences = CoreManager.getContext().getSharedPreferences(getMainUid() + "", Context.MODE_PRIVATE);
            preferences.edit().putLong("qysdk_childUserID", childUserId).apply();
            preferences.edit().putString("qysdk_childUserName", childUserName).apply();
            preferences.edit().putString("qysdk_QYAccount", childQYAcount).apply();
        }
    }

    public void setLastLoginAccount(AuthModel lastLoginAccount) {
        if (lastLoginAccount != null) {
            SharedPreferences preferences = CoreManager.getContext().getSharedPreferences(getMainUid() + "", Context.MODE_PRIVATE);
            preferences.edit().putLong("qysdk_childUserID", lastLoginAccount.getUserID()).apply();
        }
    }

}
