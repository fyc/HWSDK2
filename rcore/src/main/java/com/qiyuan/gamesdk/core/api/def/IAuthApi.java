package com.qiyuan.gamesdk.core.api.def;

import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.model.BalanceInfo;
import com.qiyuan.gamesdk.model.CouponCountInfo;
import com.qiyuan.gamesdk.model.CouponInfo;
import com.qiyuan.gamesdk.model.GameDiscountInfo;
import com.qiyuan.gamesdk.model.GamePackages;
import com.qiyuan.gamesdk.model.GetCouponInfo;
import com.qiyuan.gamesdk.model.InventoriesInfo;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qiyuan.gamesdk.model.BalanceInfo;
import com.qiyuan.gamesdk.model.CouponCountInfo;
import com.qiyuan.gamesdk.model.CouponInfo;
import com.qiyuan.gamesdk.model.GameDiscountInfo;
import com.qiyuan.gamesdk.model.GamePackages;
import com.qiyuan.gamesdk.model.GetCouponInfo;
import com.qiyuan.gamesdk.model.InventoriesInfo;

/**
 * Created by win on 17/4/26.
 */
public interface IAuthApi extends IApiWrapping {

    public static final int VCODE_TYPE_REGISTER = 2;
    public static final int VCODE_TYPE_REAL_VERIFY = 3;
    public static final int VCODE_TYPE_RESET_PASSWORD = 4;
    public static final int VCODE_TYPE_BIND_POHNE = 5;
    public static final int VCODE_TYPE_UNBIND_PHONE = 6;
    public static final int VCODE_TYPE_RESET_PAY_PASSWORD = 7;

    /**
     * 用户注册
     *
     * @param password   明文密码
     */
    void registerByUserName(String password,String userName, QyRespListener<AuthModel> callback);

    /**
     * 手机注册
     *
     * @param phone            手机号
     * @param password         明文密码
     * @param verificationCode 短信验证码
     */
    void registerByPhone(String phone, String password, String verificationCode, QyRespListener<AuthModel> callback);

    void registerChildAccount(String childUserName, QyRespListener<AuthModel.childAccount> callback);

    /**
     * 获取验证码
     *
     * @param phone  手机号
     * @param type  //1-保留，2-注册，3-实名认证，4-找回密码,5-解绑手机，6-解除绑定手机
     */
    void requestVerificationCode(String phone, int type, QyRespListener<Void> callback);

    /**
     * 获取验证码
     *
     * @param phone  手机号
     * @param type  //1-保留，2-注册，3-实名认证，4-找回密码,5-解绑手机，6-解除绑定手机
     * @param retry  重试次数
     */
    void requestVerificationCode(String phone, int type, int retry, QyRespListener<Void> callback);
    void requestVerificationCode2(String phone, int type, int retry, QyRespListener<String> callback);
    /**
     * 账号登录，用于用户手工登录
     *
     * @param account 账号
     * @param pwd     明文密码
     */
    void login(String account, String pwd,QyRespListener<AuthModel> callback);
    void login2(String account, String code,QyRespListener<LoginBean> callback);
    void loginVisitors(QyRespListener<LoginBean> callback);
    void loginAuto(QyRespListener<LoginBean> callback);
    /**
     * 登出当前账号带CP回调
     */
    void logout(IOperateCallback<String> callback);

    /**
     * 登出当前账号
     */
    void logout();
    /**
     * 获取当前用户登录态
     * 登录前为null时
     *
     * @return 登录态
     */
    String getSession();

    /**
     * 获取当前用户uid
     * 登录前为0
     *
     * @return uid
     */
    long getMainUid();

    long getSubUid();

    /**
     * @return 用户名
     */
    String getUserName();

    String getSubUserName();

    String getPhone();

    /**
     * @return 是否登录
     */
    boolean isLogin();

    /**
     * 获取余额
     * @param callback
     */
    void getBalance(QyRespListener<BalanceInfo> callback);

    /**
     * 获取优惠券列表
     * @param type 优惠券状态('UNUSED':未使用,'USED':已使用,'EXPIRED':已过期,'DISABLE':失效)
     * @param callback
     */
    void getCouponInfos(String type ,QyRespListener<CouponInfo> callback);

    /**
     * 获取优惠券数量
     * @param type 优惠券状态('UNUSED':未使用,'USED':已使用,'EXPIRED':已过期,'DISABLE':失效)
     * @param callback
     */
    void getCouponCount(String type ,QyRespListener<CouponCountInfo> callback);


    /**
     * 小号登录事件
     */
    void childAccountEvent();

    /**
     * 优惠券页面
     */
    void getCouponCenter(QyRespListener<CouponInfo> callback);

    /**
     * 小号交易列表
     */
    void getInventories(QyRespListener<InventoriesInfo> callback);

    /**
     * 获取礼包列表
     */
    void getGamePackage(QyRespListener<GamePackages> callback);

    /**
     * 领取礼包
     * @param packageId  礼包ID
     */
    void receiveGamePackage(String packageId, QyRespListener<GamePackages.GamePackageInfo> callback);


    void getGamePackageDetail(String packageId, QyRespListener<GamePackages.GamePackageInfo> callback);

    void requestGameDiscount(QyRespListener<GameDiscountInfo> callback);

    void getCoupon(int actId,QyRespListener<GetCouponInfo> callback);

    void getCouponByRule(int ruleId,QyRespListener<GetCouponInfo> callback);

    AuthModel getAuth();
}
