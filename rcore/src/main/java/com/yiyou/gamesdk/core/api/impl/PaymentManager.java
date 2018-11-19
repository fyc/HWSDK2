package com.yiyou.gamesdk.core.api.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.gson1.Gson;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.event.BackToMainFragmentEvent;
import com.qygame.qysdk.outer.event.EventDispatcherAgent;
import com.qygame.qysdk.outer.event.StartActivityEvent;
import com.qygame.qysdk.outer.model.PaymentInfo;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.StringUtils;
import com.yiyou.gamesdk.core.CoreManager;
import com.yiyou.gamesdk.core.api.ApiFacade;
import com.yiyou.gamesdk.core.api.def.IPaymentApi;
import com.yiyou.gamesdk.core.api.impl.payment.PaymentAdapter;
import com.yiyou.gamesdk.core.base.http.RequestHelper;
import com.yiyou.gamesdk.core.base.http.RequestManager;
import com.yiyou.gamesdk.core.base.http.utils.HttpUtils;
import com.yiyou.gamesdk.core.base.http.utils.Urlpath;
import com.yiyou.gamesdk.core.base.http.volley.HwJsonResquest;
import com.yiyou.gamesdk.core.base.http.volley.HwRequest;
import com.yiyou.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.yiyou.gamesdk.core.consts.StatusCodeDef;
import com.yiyou.gamesdk.core.ui.fragment.PayCenterFragment;
import com.yiyou.gamesdk.core.ui.fragment.PayFragment;
import com.yiyou.gamesdk.model.InternalOrderInfo;
import com.yiyou.gamesdk.model.PayInfo;
import com.yiyou.gamesdk.util.ExtraDef;
import com.yiyou.gamesdk.util.ToastUtils;
import com.yiyou.gamesdk.util.VersionUtil;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by levyyoung on 15/6/8.
 * 发起订单
 */
public class PaymentManager implements IPaymentApi {

    private static final String TAG = "RSDK:PaymentManager";

    private class OrderCache {
        public IOperateCallback<String> callback = null;
        public String cpOrderId = null;

        public OrderCache() {
        }

        public void clear() {
            callback = null;
            cpOrderId = null;
        }

        public boolean isEmpty() {
            return callback == null && cpOrderId == null;
        }

    }

    private final OrderCache orderCache = new OrderCache();
    private static final String PAYMENT_WEB_CLZ = PayFragment.class.getName();
    private WeakReference<Activity> paymentActivityHolder;

    @Override
    public synchronized void order(PaymentInfo paymentInfo, @Nullable Activity startUpActivity, IOperateCallback<String> callback) {

        if (StringUtils.isBlank(paymentInfo.getCpOrderId())) {
            Log.e(TAG, "Error order. invalid cp order id.");
            return;
        }

        //break by old order
        if (!orderCache.isEmpty()) {
            ToastUtils.showMsg("请勿重复操作.");
            return;
        }
        //update order cache
        orderCache.callback = callback;
        orderCache.cpOrderId = paymentInfo.getCpOrderId();

        Map<String, String> params = new ArrayMap<>();
        RequestHelper.buildParamsWithBaseInfo(params);
        String sdk_version = VersionUtil.getSdkVersion();
        String core_version = VersionUtil.getCoreVersion();
        params.put("serverID", paymentInfo.getServerId());
        params.put("userID", String.valueOf(ApiFacade.getInstance().getMainUid()));
        params.put("childUserID", String.valueOf(ApiFacade.getInstance().getSubUid()));
        params.put("userName", ApiFacade.getInstance().getUserName());
        params.put("sdkVersion", sdk_version + "-C" + core_version);
        params.put("cpOrderID", paymentInfo.getCpOrderId());
        params.put("cpFee", String.valueOf(paymentInfo.getCpFee()));
        params.put("subject", paymentInfo.getSubject());
        params.put("body", paymentInfo.getBody());
        params.put("exInfo", paymentInfo.getExInfo());
        params.put("cpCallbackUrl", paymentInfo.getCpCallbackUrl());
        params.put("accessToken", ApiFacade.getInstance().getSession());

        // TODO: 2017/11/14 新支付
        HwRequest<InternalOrderInfo> request = new HwRequest<>(Urlpath.PAYMENT_ORDER, params, InternalOrderInfo.class, new OrderRespListener(paymentInfo.getCpOrderId()));
        RequestManager.getInstance(CoreManager.getContext()).addRequest(request, null);

    }

    @Override
    public void orderH5(@Nullable Activity startUpActivity, String referer, String payUrl, IOperateCallback<String> orderCallback) {
        initPayWebView(startUpActivity, referer, payUrl);
    }

    @Override
    public void clearOrderCache() {
        orderCache.clear();
    }

    @Override
    public synchronized void notifyOrderState(final int payState, final String orderInfo) {

        paymentActivityHolder.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IOperateCallback<String> callback = orderCache.callback;
                if (callback == null) {
                    Log.w(TAG, "fail to notify order state => callback not found.");
                    orderCache.clear();
                    return;
                }
                ToastUtils.showMsg(orderInfo);
                callback.onResult(payState, orderInfo);
                orderCache.clear();
            }
        });


//        IOperateCallback<String> callback = orderCache.callback;
//        if (callback == null) {
//            Log.w(TAG, "fail to notify order state => callback not found.");
//            orderCache.clear();
//            return;
//        }
//        callback.onResult(payState, orderInfo);
//        orderCache.clear();
    }

    /**
     * 通过客户端调用三方sdk支付
     * 默认不是sdk充值钱包
     *
     * @param payWay
     * @param dataJsonStr
     */
    @Override
    public void orderThroughClient(int payWay, String dataJsonStr) {
        orderThroughClient(payWay, dataJsonStr, false);
    }

    /**
     * 通过客户端调用三方sdk支付
     *
     * @param payWay
     * @param dataJsonStr
     * @param isFromApp   是否通过sdk充值钱包
     */
    @Override
    public void orderThroughClient(int payWay, String dataJsonStr, final boolean isFromApp) {
        Activity paymentActivity = paymentActivityHolder.get();
        if (paymentActivity == null) {
            releaseOrderCallbackOnFail(QYCodeDef.ERROR_PAY_CONTEXT, "调起第三方支付失败");
            return;
        }
        PaymentAdapter.sharedInstance().startPay(payWay, paymentActivity, dataJsonStr, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String orderInfo) {
                notifyOrderState(i, orderInfo);
                Activity paymentActivity = paymentActivityHolder.get();
                if (paymentActivity != null && !paymentActivity.isFinishing()) {
                    if (isFromApp) {
                        EventDispatcherAgent.defaultAgent().broadcast(BackToMainFragmentEvent.TYPE_BACK_TO_MAIN_FRAGMENT,
                                new BackToMainFragmentEvent.Param(0, paymentActivity, null));
                    } else {
                        paymentActivity.finish();
                    }
                }
            }
        });
    }

    /**
     * 更新支付页面引用。 部分三方支付SDK接口需要此数据。
     *
     * @param activity
     */
    @Override
    public void updatePaymentActivity(Activity activity) {
        if (paymentActivityHolder != null) {
            paymentActivityHolder.clear();
        }
        if (activity == null) {
            Log.w(TAG, "skip update payment activity - invalid activity.");
        }
        paymentActivityHolder = new WeakReference<Activity>(activity);
    }

    @Override
    public void closeNotify() {//关闭页面时候回调
        Log.d(TAG, "click close");
        if (paymentActivityHolder.get() != null) {
            paymentActivityHolder.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    releaseOrderCallbackOnFail(QYCodeDef.ERROR_USER_CANCEL_ORDER, "cancel pay");

                }
            });
        }
//        releaseOrderCallbackOnFail(QYCodeDef.ERROR_USER_CANCEL_ORDER, "cancel pay");
    }

    @Override
    public void orderPay(Map<String, String> params, QyRespListener<String> callback) {
        params.put("accessToken", ApiFacade.getInstance().getSession());
        String url = HttpUtils.buildUrl(Urlpath.PAY_URL, params);
        HwRequest request = new HwRequest<>(url, null, String.class, callback);
        RequestManager.getInstance(CoreManager.getContext()).addRequest(request, null);
    }

    private class OrderRespListener extends QyRespListener<InternalOrderInfo> {
        private String cpOrderId;

        public OrderRespListener(String orderId) {
            cpOrderId = orderId;
        }

        @Override
        public void onNetSucc(String url, Map<String, String> params, InternalOrderInfo result) {
            super.onNetSucc(url, params, result);
            //error processing
            if (result == null) {
                Log.e(TAG, "wtf! order error => invalid result.");
                releaseOrderCallbackOnFail(StatusCodeDef.UNKNOWN, cpOrderId);
                return;
            }
            if (result.getCpOrderID() == null || !result.getCpOrderID().equals(cpOrderId)) {
                Log.e(TAG, "wtf! order error => unmatched.");
                releaseOrderCallbackOnFail(StatusCodeDef.UNKNOWN, cpOrderId);
                return;
            }
            if (StringUtils.isBlank(result.getPayUrl())) {
                Log.e(TAG, "wtf! invalid payment url.");
                releaseOrderCallbackOnFail(StatusCodeDef.UNKNOWN, cpOrderId);
                return;
            }

            //result processing : jump web view
//            String paymentUrl = result.getPayUrl();
//            Bundle arguments = new Bundle();
//            arguments.putString(ExtraDef.EXTRA_URL, paymentUrl);
//            StartActivityEvent.FragmentParam param = new StartActivityEvent.FragmentParam(0, CoreManager.getContext(),
//                    StartActivityEvent.DISPLAY_TYPE_FULLSCREEN, null, PAYMENT_WEB_CLZ, arguments);
//            EventDispatcherAgent.defaultAgent().broadcast(StartActivityEvent.TYPE_START_ACTIVITY_ENVENT, param);

//            ToastUtils.showMsg(result.getOrderInfoUrl());
            HwRequest<PayInfo> request = new HwRequest<>(result.getOrderInfoUrl(), params, PayInfo.class, new QyRespListener<PayInfo>() {
                @Override
                public void onNetSucc(String url, Map params, PayInfo result) {
                    Bundle arguments = new Bundle();
                    Gson gson = new Gson();
                    String str = gson.toJson(result);
                    android.util.Log.d(TAG, "onNetSucc: " + str);
                    arguments.putString(ExtraDef.EXTRA_PAY_INFO, str);
                    StartActivityEvent.FragmentParam fragmentParam = new StartActivityEvent.FragmentParam(0, CoreManager.getContext(),
                            StartActivityEvent.DISPLAY_TYPE_DIALOG,
                            null, PayCenterFragment.class.getName(), arguments);
                    EventDispatcherAgent.defaultAgent().broadcast(StartActivityEvent.TYPE_START_ACTIVITY_ENVENT, fragmentParam);
                }

                @Override
                public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                    super.onNetError(url, params, errno, errmsg);
                    releaseOrderCallbackOnFail(StatusCodeDef.NetworkError, errmsg);
                }

                @Override
                public void onFail(int errorNo, String errmsg) {
                    super.onFail(errorNo, errmsg);
                    releaseOrderCallbackOnFail(errorNo, errmsg);
                }
            });
            RequestManager.getInstance(CoreManager.getContext()).addRequest(request, null);

        }

        @Override
        public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
            super.onNetError(url, params, errno, errmsg);
            Log.e(TAG, "order network error => error no:" + errno + ", error msg:" + errmsg);
            releaseOrderCallbackOnFail(StatusCodeDef.NetworkError, errmsg);
        }

        @Override
        public void onFail(int errorNo, String errmsg) {
            super.onFail(errorNo, errmsg);
            Log.e(TAG, "order fail => error no:" + errorNo + ",error msg:" + errmsg);
            releaseOrderCallbackOnFail(errorNo, errmsg);
        }
    }

    private synchronized void releaseOrderCallbackOnFail(int errorCode, String msg) {
        if (!orderCache.isEmpty()) {
            Log.d(TAG, "releaseOrderCallbackOnFail: ");
            orderCache.callback.onResult(errorCode, msg);
            orderCache.clear();
        }
    }

    @Override
    public void getOrderFromApp(Map<String, String> params) {
        HwJsonResquest jsonResquest = new HwJsonResquest(Urlpath.FROM_APP_OLDER, params, new QyRespListener<JSONObject>() {
            @Override
            public void onNetSucc(String url, Map<String, String> params, JSONObject result) {
                JSONObject head = result.optJSONObject("head");
                String code = head.optString("result");
                if (code.equals("0")) {
                    String dataJsonStr = result.optString("body");
                    Log.d(TAG, "js invoke Pay - data : " + dataJsonStr);
                    if (params.get("payChannel").equals("ALIPAY")) {
                        ApiFacade.getInstance().orderThroughClient(PaymentAdapter.PAY_WAY_ALI_PAY, dataJsonStr, true);
                    } else {
                        ApiFacade.getInstance().orderThroughClient(PaymentAdapter.PAY_WAY_IPAYNOW, dataJsonStr, true);
                    }
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                Log.d(TAG, "url: " + url + " params: " + params + " errno: " + errno + "" + "errmsg: " + errmsg);
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                Log.d(TAG, " errno: " + errorNo + "errmsg: " + errmsg);
            }
        });

        RequestManager.getInstance(CoreManager.getContext()).addRequest(jsonResquest, null);
    }

    WebView payWebView;

    protected void initPayWebView(final Activity activity, final String Referer, String payUrl) {
        payWebView = new WebView(activity);
        payWebView.setBackgroundColor(2);
        payWebView.requestFocus();
        payWebView.setVerticalScrollBarEnabled(true);
        payWebView.loadUrl(payUrl);
        payWebView.getSettings().setJavaScriptEnabled(true);

        payWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        payWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        payWebView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = activity.getFilesDir().getAbsolutePath() + "payCache";

        //设置数据库缓存路径
        payWebView.getSettings().setDatabasePath(cacheDirPath);
        //开启 Application Caches 功能
        payWebView.getSettings().setAppCacheEnabled(true);


        payWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        payWebView.setWebChromeClient(new WebChromeClient() {
            //                @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }

            //                @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }


        });


        payWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                Log.e(TAG, "url=>" + url);
                if (url.startsWith("https://mclient.alipay.com/")) {
                    view.loadUrl(url);
                } else if (url.startsWith("alipays://platformapi/startApp?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                    if (payWebView != null) {
                        payWebView.removeAllViews();
                        payWebView.destroy();
                        payWebView = null;
                    }
                    return true;
                }
                /**
                 * 判断是否成功拦截
                 * 若成功拦截，则无需继续加载该URL；否则继续加载
                 */
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                else if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                    Log.e(TAG, "url=>4");
                    if (payWebView != null) {
                        payWebView.removeAllViews();
                        payWebView.destroy();
                        payWebView = null;
                    }
                    return true;
                } else {
                    Map<String, String> extraHeaders = new HashMap<String, String>();
                    extraHeaders.put("Referer", Referer);
                    view.loadUrl(url, extraHeaders);
                    Log.e(TAG, "url=>5");
                }
                Log.e(TAG, "url=>6");
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //此方法是为了处理在5.0以上Htts的问题，必须加上
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                new AlertDialog.Builder(activity).setTitle("网络连接失败")//设置对话框标题
                        .setMessage("请重试")//设置显示的内容
                        .setPositiveButton("刷新", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                payWebView.reload();
                            }

                        }).show();//在按键响应事件中显示此对话框
            }
        });
        final ViewGroup view = (ViewGroup) activity.getWindow().getDecorView();
        view.addView(payWebView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        MainActivity.payWebView.addJavascriptInterface(new JavaCallObject(), "javaCallJs");
//        MainActivity.payWebView.addJavascriptInterface(new JavaPayCallObject(mainActivity), "payCallJs");
    }
}
