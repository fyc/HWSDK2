package com.qiyuan.gamesdk.core.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.utils.Urlpath;
import com.qiyuan.gamesdk.util.CommonUtils;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.utils.Urlpath;
import com.qiyuan.gamesdk.util.CommonUtils;

import java.util.Locale;

/**
 * Created by BM on 2017/11/27.
 * <p>
 * desc:
 */

public class UnInternalLinkFragment extends BaseFragment{

    @Override
    protected void setFragmentContent(Context content, ViewGroup container, Fragment titleBarFragment) {
        View layout = LayoutInflater.from(content).inflate(R.layout.sdk_user_center_un_download_qiyuan, container, false);
        container.addView(layout);
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.handleUrl(String.format(Locale.getDefault(), Urlpath.DOWNLOAD_APP, ApiFacade.getInstance().getChannel()),
                        getActivity(), true);
            }
        };
        layout.findViewById(R.id.download_qiyuan).setOnClickListener(l);
    }
}
