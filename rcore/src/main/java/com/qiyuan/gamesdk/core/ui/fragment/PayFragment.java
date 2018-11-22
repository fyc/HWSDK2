package com.qiyuan.gamesdk.core.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.ui.common.CommonTitlePrimaryFragment;
import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;
import com.qiyuan.gamesdk.util.ExtraDef;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.ui.common.CommonTitlePrimaryFragment;
import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;
import com.qygame.qysdk.outer.util.StringUtils;
import com.qiyuan.gamesdk.util.ExtraDef;

/**
 * Created by chenshuide on 15/7/15.
 */
public class PayFragment extends WebFragment {
    //    private IEventListener backListener;
    private static final String TAG = "QYSDK:PayFragment";

    @Override
    protected void afterSetFragmentContent(Context context, Fragment titleBarFragment) {
        super.afterSetFragmentContent(context, titleBarFragment);
        ApiFacade.getInstance().updatePaymentActivity(getActivity());
    }

    @Override
    public NativeTitleBarUpdateInfo getTitleBarConfig() {
        NativeTitleBarUpdateInfo titleBarUpdateInfo = new NativeTitleBarUpdateInfo();
        titleBarUpdateInfo.showBackButton = false;
//        titleBarUpdateInfo.showRefreshButton = false;
        titleBarUpdateInfo.showCloseButton = true;
        titleBarUpdateInfo.showConfirmButton = false;
        return titleBarUpdateInfo;
    }

    @Override
    public String getUrl() {
        String autoLoadPageUrl = getArguments().getString(ExtraDef.EXTRA_URL);
        if (!StringUtils.isBlank(autoLoadPageUrl)) {
            return autoLoadPageUrl;
        }
        return "about:blank";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected Fragment getTitleBarFragment() {
        return CommonTitlePrimaryFragment.newInstance(getString(R.string.tt_pay_center));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        ApiFacade.getInstance().closeNotify();
        return super.onBackPressed();
    }
}
