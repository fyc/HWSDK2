package com.yiyou.gamesdk.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.gamesdk.shouyouba.tzsy.R;
import com.mobilegamebar.rsdk.container.RGameSDK;
import com.mobilegamebar.rsdk.outer.IOperateCallback;
import com.mobilegamebar.rsdk.outer.model.PaymentInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LY on 15/7/28.
 */
public class PayFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PayFragment";
    private EditText input;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_pay, container, false);
        Button button1 = (Button) layout.findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = (Button) layout.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = (Button) layout.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 = (Button) layout.findViewById(R.id.button4);
        button4.setOnClickListener(this);
        Button button5 = (Button) layout.findViewById(R.id.button5);
        button5.setOnClickListener(this);
        Button button6 = (Button) layout.findViewById(R.id.button6);
        button6.setOnClickListener(this);

        input = (EditText) layout.findViewById(R.id.input);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                pay(0.1);
                break;

            case R.id.button2:
                pay(0.2);

                break;

            case R.id.button3:
                pay(0.5);

                break;

            case R.id.button4:
                pay(0.8);

                break;

            case R.id.button5:
                pay(1.0);

                break;
            case R.id.button6:


                String fee = input.getText().toString().trim();
                if (TextUtils.isEmpty(fee)) {

                    Toast.makeText(getActivity(), "输入金额不能为空", Toast.LENGTH_LONG).show();

                    return;
                }

                Number fee1 = Float.valueOf(fee);


//                pay(fee1);
                pay2("");
                break;


        }
    }

    public boolean onBackPressed(){
        if(payWebView!=null){
            payWebView.removeAllViews();
            payWebView.destroy();
            payWebView = null;
            return true;
        }
        return false;
    }

    private void pay(Number fee) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setBody("很厉害的的奇药");
        paymentInfo.setCpFee(fee);
        paymentInfo.setCpOrderId("cp_order_" + new Date().getTime());
        paymentInfo.setServerId("一区 东皇太一");
        paymentInfo.setExInfo("扩展信息");
        paymentInfo.setSubject("以太药剂");
        paymentInfo.setPayMethod(PaymentInfo.PAY_METHOD_ALL);
        paymentInfo.setCpCallbackUrl("http://120.132.68.148/r-imitateCpServer/callback.jsp");
        paymentInfo.setChargeDate(new Date().getTime());
        RGameSDK.getInstance().pay(getActivity(), paymentInfo, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String orderInfo) {
                Log.d(TAG, "onResult: " + i + " ; " + Thread.currentThread().getName());
            }
        });
    }

    private void pay2(String payUrl) {
        payUrl = "http://www.373yx.com/payment/preview?cliBuyerId=19000&cliSellerId=201810221735413530001013eed&" +
                "cpOrderNo=" + System.currentTimeMillis() +
                "&cpPrice=0.01&cpOrderTitle=%E9%A6%96%E5%85%851";
//        RGameSDK.getInstance().pay2(getActivity(), payUrl, new IOperateCallback<String>() {
//            @Override
//            public void onResult(int i, String orderInfo) {
//                Log.d(TAG, "onResult: " + i + " ; " + Thread.currentThread().getName());
//            }
//        });
        initPayWebView(getActivity(), payUrl);
    }

    WebView payWebView;

    protected void initPayWebView(final Activity activity, String payUrl) {
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
//                if (!(url.startsWith("http") || url.startsWith("https"))) {
//                    return true;
//                }
                com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>" + url);
                /**
                 * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
                 */
                final PayTask task = new PayTask(activity);
                com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>1");
                boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                    @Override
                    public void onPayResult(final H5PayResultModel result) {
                        // 支付结果返回
                        final String url = result.getReturnUrl();
                        Log.e(TAG, "url=>aaaaa" + url);
                        if (!TextUtils.isEmpty(url)) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.loadUrl(url);
                                }
                            });
                        }
                    }
                });
                com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>2");
                /**
                 * 判断是否成功拦截
                 * 若成功拦截，则无需继续加载该URL；否则继续加载
                 */
                if (!isIntercepted) {
//                    view.loadUrl(url);
                    com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>3");
                    // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                    if (url.startsWith("weixin://wap/pay?")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        activity.startActivity(intent);
                        com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>4");
                        return true;
                    } else {
                        Map<String, String> extraHeaders = new HashMap<String, String>();
                        extraHeaders.put("Referer", "http://www.373yx.com");
                        view.loadUrl(url, extraHeaders);
                        com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>5");
                    }
                }
                com.mobilegamebar.rsdk.outer.util.Log.e(TAG, "url=>6");
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
                if (url.contains("alipay.com")) {
//                    isAlipay = true;
                } else {
//                    isAlipay = false;
                }
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
