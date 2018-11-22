package com.qiyuan.gamesdk.testapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gamesdk.shouyouba.tzsy.R;
import com.qygame.qysdk.container.QYGameSDK;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.model.PaymentInfo;

import java.util.Date;

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
                payH5("");
                break;


        }
    }

    public boolean onBackPressed() {
//        if (payWebView != null) {
//            payWebView.removeAllViews();
//            payWebView.destroy();
//            payWebView = null;
//            return true;
//        }
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
        QYGameSDK.getInstance().pay(getActivity(), paymentInfo, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String orderInfo) {
                Log.d(TAG, "onResult: " + i + " ; " + Thread.currentThread().getName());
            }
        });
    }

    private void payH5(String payUrl) {
        payUrl = "http://www.373yx.com/payment/preview?cliBuyerId=19000&cliSellerId=2018111415564890400010102c2&" +
                "cpOrderNo=" + System.currentTimeMillis() +
                "&cpPrice=0.01&cpOrderTitle=%E9%A6%96%E5%85%851";
        QYGameSDK.getInstance().payH5(getActivity(), 19000L, "2018111415564890400010102c2", System.currentTimeMillis() + "", "%E9%A6%96%E5%85%851", 0.01f);
    }
}
