package com.qiyuan.gamesdk.core.interceptor;

import android.content.Context;

import com.qiyuan.gamesdk.model.GameUpdateInfo;
import com.qiyuan.gamesdk.util.PackageUtil;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.GameUpdateInfo;
import com.qygame.qysdk.outer.util.Log;
import com.qiyuan.gamesdk.util.PackageUtil;

import java.util.Map;

/**
 * 强更拦截器
 * Created by shui on 4/11/16.
 */
public class ForceInterceptor implements InitInterceptor {

    private static final String TAG = "QYSDK: ForceInterceptor";

    @Override
    public void intercept(Chain<InitParams> chain) {

        forceUpgradeCheck(chain);

    }


    private void forceUpgradeCheck(final Chain<InitParams> chain) {

        Context activity = chain.getData().getActivity();

        Log.d(TAG, "begin check force upgrade");

        ApiFacade instance = ApiFacade.getInstance();
        instance.upgradeRequest("", instance.getCurrentGameID(), PackageUtil.getVersionName(activity),
                PackageUtil.getVersionCode(activity) + "", new QyRespListener<GameUpdateInfo>() {

                    @Override
                    public void onNetSucc(String url, Map<String, String> params, GameUpdateInfo result) {

                        chain.proceed(chain.getData().setGameUpdateInfo(result));



                    }

                    @Override
                    public void onFail(int errorNo, String errmsg) {
                        Log.d(TAG, "requestUpgrade" + errmsg);
                        chain.proceed(chain.getData());
                    }


                    @Override
                    public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                        Log.d(TAG, "requestUpgrade" + errmsg);
                        chain.proceed(chain.getData());

                    }
                });
    }
}
