package com.yiyou.gamesdk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

    private Context mContext;
    private View mPopWindow;
    private TextView txtToastMessage, txt_change_account;

    public static interface PopOnClick {
        public void onClick(View v);
    }

    public PopUtil(Context context, String text, final PopOnClick popOnClick) {
        this.mContext = context;
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        mPopWindow = inflater.inflate(R.layout.login_pop_toast, null);//使用LoginActivity加载不到布局资源
        mPopWindow = LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.login_pop_toast, null);
        txtToastMessage = (TextView) mPopWindow.findViewById(R.id.txtToastMessage);
        txt_change_account = (TextView) mPopWindow.findViewById(R.id.txt_change_account);
        txtToastMessage.setText(text);
        txt_change_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popOnClick != null) {
                    popOnClick.onClick(v);
                }
            }
        });
        setmPopWindow(text);
    }

    private void setmPopWindow(String text) {
        // 把View添加到PopWindow中
        this.setContentView(mPopWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(dip2px(mContext, 60));
        //  设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //   设置背景透明
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    public static void show(final Activity activity, String str, PopOnClick popOnClick) {
        final PopUtil popUtil = new PopUtil(activity, str, popOnClick);
        popUtil.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.TOP, 0, 0);
//        new CountDownTimer(2000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                popUtil.showAtLocation(activity.getWindow().getDecorView(),
//                        Gravity.TOP, 0, 0);
//            }
//            @Override
//            public void onFinish() {
//                popUtil.dismiss();
//            }
//        }.start();
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
