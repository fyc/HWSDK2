package com.yiyou.gamesdk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

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
    public TextView txtToastMessage, txt_change_account;

    public interface PopOnClick {
        void onClick(View v);
    }

    public PopUtil(Activity activity) {
        this.activity = activity;
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        mPopWindow = inflater.inflate(R.layout.login_pop_toast, null);//使用LoginActivity加载不到布局资源
        mPopWindow = LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.login_pop_toast, null);
        txtToastMessage = (TextView) mPopWindow.findViewById(R.id.txtToastMessage);
        txt_change_account = (TextView) mPopWindow.findViewById(R.id.txt_change_account);
        setmPopWindow();
    }

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
     * @param str
     * @return
     */
    public PopUtil showNoButton(String str) {
        txtToastMessage.setText(str);
        txt_change_account.setVisibility(View.GONE);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
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
     * @param str
     * @param popOnClick
     * @return
     */
    public PopUtil showHasButton(String str, final PopOnClick popOnClick) {
        txtToastMessage.setText(str);
        txt_change_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popOnClick != null) {
                    popOnClick.onClick(v);
                }
            }
        });
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
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


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
