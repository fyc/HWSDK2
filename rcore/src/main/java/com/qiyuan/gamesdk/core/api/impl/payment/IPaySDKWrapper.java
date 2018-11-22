package com.qiyuan.gamesdk.core.api.impl.payment;

import android.app.Activity;

import com.qygame.qysdk.outer.IOperateCallback;

/**
 * Created by levyyoung on 15/9/16.
 */
public interface IPaySDKWrapper {

    void startPay(Activity startUpActivity, String paymentData, IOperateCallback<String> callback);

}
