package com.qiyuan.gamesdk.core.api.impl.payment;

import android.app.Activity;
import android.os.AsyncTask;

import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;
import com.qiyuan.gamesdk.util.ToastUtils;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.util.Log;
import com.qiyuan.gamesdk.util.ToastUtils;


/**
 * Created by LY on 2015/10/12.
 */
public class IPayNowWrapper implements IPaySDKWrapper{
    private static final String TAG = "QYSDK:IPayNow";
    private static Activity act = null;
    @Override
    public void startPay(Activity startUpActivity, String paymentData, IOperateCallback<String> callback) {
        if (startUpActivity == null) {
            Log.e(TAG, "no start up activity for payment.");
            if (callback != null) {
                callback.onResult(QYCodeDef.ERROR_NO_START_UP_ACTIVITY_FOR_PAY, "支付失败");
            }
            return;
        }
        act = startUpActivity;
        IpaynowPlugin.getInstance().init(act);
        GetMessage gM = new GetMessage();
        gM.execute(paymentData);
    }

    public static class GetMessage extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String needcheckmsg = params[0];
            return needcheckmsg;
        }

        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            IpaynowPlugin.getInstance().setCallResultReceiver(new ReceivePayResult() {
                @Override
                public void onIpaynowTransResult(ResponseParams responseParams) {
                    if(responseParams != null){
                        String temp;
                        String respCode = responseParams.respCode;
                        String errorCode = responseParams.errorCode;
                        String respMsg = responseParams.respMsg;

                        Log.d(TAG, String.format("respCode %s, errorCode %s ,respMsg %s", respCode, errorCode,
                                respMsg + ""));


                        if (respCode.equals("00")) { //交易状态:成功
                            temp ="交易状态:成功";
                            PaymentAdapter.sharedInstance()
                                    .notifyPayResult(QYCodeDef.SUCCESS, temp);
                        } else if (respCode.equals("02")) {//交易状态:取消
                            temp ="交易状态:取消";
                            PaymentAdapter.sharedInstance()
                                    .notifyPayResult(QYCodeDef.IPAY_NOW_RESULT_CANCEL, temp);
                        } else if (respCode.equals("01")) {//交易状态:失败
                            temp="交易状态:失败"+"\n"+"错误码:"+errorCode+"原因:" + respMsg;
                            PaymentAdapter.sharedInstance()
                                    .notifyPayResult(QYCodeDef.IPAY_NOW_RESULT_FAIL, temp);
                            if(errorCode.equals("E004")){
                                if(respMsg.contains("3000")){
                                    ToastUtils.showMsg("已超过单笔限额:3000.00元，您可使用支付宝充值");
                                }else if(respMsg.contains("6000")){
                                    ToastUtils.showMsg("已超过单笔限额:6000.00元，您可使用支付宝充值");
                                }
                            }
                        } else if (respCode.equals("03")) {//交易状态:未知
                            temp = "respCode="+respCode+"\n"+"respMsg="+respMsg;
                            PaymentAdapter.sharedInstance()
                                    .notifyPayResult(QYCodeDef.ERROR_ORDER_FAIL_ALL_IN_PAY, temp);
                        } else { //其他
                            temp = "respCode="+respCode+"\n"+"respMsg="+respMsg;
                            PaymentAdapter.sharedInstance()
                                    .notifyPayResult(QYCodeDef.ERROR_ORDER_FAIL_ALL_IN_PAY, temp);
                        }
                        ToastUtils.showMsg(temp);
                    }
                }
            });
            IpaynowPlugin.getInstance().pay(result);  //聚合支付接口
            String name= Thread.currentThread().getName();
            Log.d(TAG,"Ipaynow pay Thread: " + name);
            Log.d(TAG, "Ipaynow pay: " + result);
        }
    }
}
