package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.qiyuan.gamesdk.core.ui.dialog.BaseViewController;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.base.http.volley.bean.LoginBean;
import com.qiyuan.gamesdk.core.ui.dialog.BaseViewController;
import com.qiyuan.gamesdk.model.AuthModel;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.ResourceHelper;

/**
 * Created by Orange on 15/6/15.
 */
public abstract class BaseAuthViewController extends BaseViewController {
    private IDialogParam dialogParam;
    private View loading;
    private View loadingAnimator;
    private boolean animationStart = false;
    private boolean willCloseByAuthResult = false;//AuthResult关闭窗口时，不进行用户取消登录的回调
    public BaseAuthViewController(Context context, IDialogParam params) {
        super(context);
        View contentView = getChildAt(0);
        LayoutParams layoutParams = (LayoutParams) contentView.getLayoutParams();
        layoutParams.addRule(CENTER_IN_PARENT);
        contentView.setLayoutParams(layoutParams);
        inflate(ResourceHelper.getContextHolder().get(),
                R.layout.qy_sdk_loading, this);
        loading = findViewById(R.id.global_loading);
        loadingAnimator = findViewById(R.id.loading_animator);
        dialogParam = params;
    }

    public synchronized void showLoading() {
        loading.setVisibility(View.VISIBLE);
        if (!animationStart) {
            animationStart = true;
            Animation animation = AnimationUtils.loadAnimation(ResourceHelper.getContextHolder().get(),
                    R.anim.anim_rotate_loading);
            loadingAnimator.startAnimation(animation);
        }
    }

    public synchronized void hideLoading() {
        loading.setVisibility(View.INVISIBLE);
    }

    void notifyAuthResult(int code, String resultMsg, AuthModel authModel) {
        willCloseByAuthResult = true;
        if(dialogParam != null) {
            if(dialogParam.getAppCallback() != null) {
                dialogParam.getAppCallback().onResult(code, resultMsg);
            }
//            if(dialogParam.getGameCallback() != null && authModel != null) {
//                dialogParam.getGameCallback().process(authModel.getAccount(), authModel.getPassword());
//            }
        }
    }

    /**
     * 执行LoginActivity中的‘登录成功回调’
     * @param code
     * @param resultMsg
     * @param loginBean
     */
    void notifyAuthResult2(int code, String resultMsg, LoginBean loginBean) {
        willCloseByAuthResult = true;
        if(dialogParam != null) {
            if(dialogParam.getAppCallback() != null) {
                dialogParam.getAppCallback().onResult(code, resultMsg);
            }
//            if(dialogParam.getGameCallback() != null && authModel != null) {
//                dialogParam.getGameCallback().process(authModel.getAccount(), authModel.getPassword());
//            }
        }
    }

    @Override
    public void onDismiss() {
        if (!willCloseByAuthResult && dialogParam != null) {
            if (dialogParam.getAppCallback() != null) {
                dialogParam.getAppCallback().onResult(QYCodeDef.ERROR_USER_CANCEL_LOGIN, ResourceHelper.getString(R.string.user_cancel_login));
            }
        }
    }

    IDialogParam getDialogParam() {
        return dialogParam;
    }
}
