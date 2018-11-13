package com.yiyou.gamesdk.core.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mobilegamebar.rsdk.outer.util.Log;
import com.yiyou.gamesdk.R;
import com.yiyou.gamesdk.core.ui.dialog.BaseViewController;

public class TipsAlertDialogView extends BaseViewController implements View.OnClickListener {

    private static final String TAG = "TTSDK: " + "TipsAlertDialogView";
    public interface Onclick {
        void onPositive();

        void onNegative();
    }

    Button btn_sure, btn_cancle;
    private Onclick onclick;

    public void setOnclick(Onclick onclick) {
        this.onclick = onclick;
    }

    public TipsAlertDialogView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        btn_cancle = (Button) findViewById(R.id.button_cancle);
        btn_cancle.setOnClickListener(this);
        btn_sure = (Button) findViewById(R.id.button_sure);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancle:
                Log.d(TAG, "button_cancle: ");
                if (onclick != null) {
                    onclick.onNegative();
                }
                close();
                break;

            case R.id.button_sure:
                Log.d(TAG, "button_sure: ");
                if (onclick != null) {
                    onclick.onPositive();
                }
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_tip_dialog;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onDismiss() {

    }
}
