package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.qygame.qysdk.outer.util.ResourceHelper;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.util.ViewUtils;

public class ReGetVerifyCodeButtonController {
    TextView textView;
    long mMillisUntilFinished = 0;
    public ReGetVerifyCodeButtonController(TextView textView) {
        this.textView = textView;
    }

    public void prepare() {
        ViewUtils.setViewEnable(textView, false);
    }

    private CountDownTimer timer;

    public void startCountDown() {
        if (timer != null) {
            return;
        }
        timer = new CountDownTimer(60 * 1000l, 1000l) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisUntilFinished =  millisUntilFinished;
                if (textView.getVisibility() == View.VISIBLE){
                    int sec = (int) (millisUntilFinished / 1000l);
                    textView.setText(ResourceHelper.getString(R.string.re_get_verification_fmt, sec));
                }
            }

            @Override
            public void onFinish() {
                timer = null;
                textView.setText(ResourceHelper.getString(R.string.re_get_verification));
                ViewUtils.setViewEnable(textView, true);
            }
        };
        timer.start();
    }

    public void cancelCountDown() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(ResourceHelper.getString(R.string.re_get_verification));
                    ViewUtils.setViewEnable(textView, true);
                }
            });
        }
    }

    public long getmMillisUntilFinished() {
        return mMillisUntilFinished;
    }
}
