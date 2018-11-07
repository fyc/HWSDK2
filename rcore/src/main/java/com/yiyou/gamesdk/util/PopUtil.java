package com.yiyou.gamesdk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobilegamebar.rsdk.outer.util.ResourceHelper;
import com.yiyou.gamesdk.R;
import com.yiyou.gamesdk.core.CoreManager;

/**
 * @author:Victory
 * @time: 2018/4/16
 * @Email:949021037@qq.com
 * @QQ:949021037
 * @explain;
 */

public class PopUtil extends PopupWindow {

    private Activity activity;//代表LoginActivity
    private View mPopWindow;
    public TextView txtToastAcount, txtToastCountTime;
    Button button_change_account;

    public interface PopOnListener {
        void onClick(View v);

        void onTick(long millisUntilFinished);

        void onFinish();
    }

    public PopUtil(Activity activity) {
        this.activity = activity;
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        mPopWindow = inflater.inflate(R.layout.login_pop_toast, null);//使用LoginActivity加载不到布局资源
        mPopWindow = LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.login_pop_toast, null);
        txtToastAcount = (TextView) mPopWindow.findViewById(R.id.txtToastAcount);
        txtToastCountTime = (TextView) mPopWindow.findViewById(R.id.txtToastCountTime);
        button_change_account = (Button) mPopWindow.findViewById(R.id.button_change_account);
        setmPopWindow();
    }

    /**
     * 初始化一些相关的参数
     */
    private void setmPopWindow() {
        // 把View添加到PopWindow中
        this.setContentView(mPopWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(dip2px(activity, 60));
        //  设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //   设置背景透明
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    public static PopUtil get(Activity activity) {
        PopUtil popUtil = new PopUtil(activity);
        return popUtil;
    }

    /**
     * @param acount
     * @return
     */
    public PopUtil showNoButton(final String acount) {
//        txtToastMessage.setText(str);
        button_change_account.setVisibility(View.GONE);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtToastAcount.setText(acount);
                txtToastCountTime.setText("欢迎");
                showAtLocation(activity.getWindow().getDecorView(),
                        Gravity.TOP, 0, 0);
            }

            @Override
            public void onFinish() {
                if (isShowing()) {
                    dismiss();
                }
            }
        }.start();
        return this;
    }

    /**
     * @param acount
     * @param popOnListener
     * @return
     */
    public PopUtil showHasButton(final String acount, final PopOnListener popOnListener) {
//        txtToastMessage.setText(str);
        final CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                String str = ResourceHelper.getString(R.string.str_visitors_welcome, result.getData().getUser_id(), millisUntilFinished / 1000 + "s");
                txtToastAcount.setText(acount);
                txtToastCountTime.setText(millisUntilFinished / 1000 + "S");
                txtToastCountTime.setTextColor(ResourceHelper.getColor(R.color.login_red));
                showAtLocation(activity.getWindow().getDecorView(),
                        Gravity.TOP, 0, 0);
                if (popOnListener != null) {
                    popOnListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (isShowing()) {
                    dismiss();
                }
                if (popOnListener != null) {
                    popOnListener.onFinish();
                }
            }
        };
        button_change_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (popOnListener != null) {
                    popOnListener.onClick(v);
                }
            }
        });
        countDownTimer.start();
        return this;
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
