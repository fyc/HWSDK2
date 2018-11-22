package com.qiyuan.gamesdk.core.ui.widget;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.qiyuan.gamesdk.core.ui.dialog.BaseViewController;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.util.AssetsUtil;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.BaseViewController;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.util.AssetsUtil;

public class TermsOfServiceAlertDialogView extends BaseViewController implements View.OnClickListener {
    private static final String TAG = "QYSDK: " + "TermsOfService";
    Context mContext;
    WebView webview_content;
    Button btn_sure;
    private IDialogParam dialogParam;
    public TermsOfServiceAlertDialogView(Context context,IDialogParam params) {
        super(context);
        mContext = context;
        dialogParam = params;
        initView();
    }

    private void initView() {
        webview_content = (WebView) findViewById(R.id.webview_content);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
//        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
//        ViewGroup.LayoutParams lp;
//        lp=webview_content.getLayoutParams();
//        lp.width=(int) (display.getWidth() * 0.8); // 宽度设置为屏幕的0.95
//        lp.height=(int) (display.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        webview_content.setLayoutParams(lp);
        webview_content.loadUrl(AssetsUtil.getHtml("terms_of_service.html"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                ViewControllerNavigator.getInstance().toLogin(dialogParam);
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_terms_of_service2;
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
