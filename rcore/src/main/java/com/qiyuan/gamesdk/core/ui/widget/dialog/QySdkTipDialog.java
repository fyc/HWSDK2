package com.qiyuan.gamesdk.core.ui.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.CoreManager;

public class QySdkTipDialog extends DialogFragment {

    public interface Onclick {
        void onPositive();

        void onNegative();
    }

    private Onclick onclick;

    Button btn_sure, btn_cancle;

    public void show(FragmentManager fragmentManager, Onclick onClick) {
        this.onclick = onClick;
        show(fragmentManager, "ZujiTrackSwitchDialog");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View view = inflater.inflate(R.layout.qy_sdk_tip_dialog, null);//使用LoginActivity加载不到布局资源
        final View view = LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.qy_sdk_tip_dialog, null);
        btn_cancle = (Button) view.findViewById(R.id.button_cancle);
        btn_sure = (Button) view.findViewById(R.id.button_sure);
        if (onclick != null) {
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclick.onPositive();
                    dismiss();
                }
            });
            btn_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclick.onNegative();
                    dismiss();
                }
            });
        }

        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
//        Window window = getDialog().getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;
//        windowParams.y = 100;
//        window.setAttributes(windowParams);
        //在此设置大小宽高
        Dialog dialog = getDialog();
        if (dialog != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //设置dialog的背景为透明
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.9),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //静态调用方法
    public static void show(FragmentActivity mActivity, Onclick onClick) {
        QySdkTipDialog dialogFragment = new QySdkTipDialog();
        dialogFragment.show(mActivity.getSupportFragmentManager(), onClick);
    }
}
