package com.qiyuan.gamesdk.core.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.qiyuan.gamesdk.util.ToastUtils;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.event.EventDispatcherAgent;
import com.qygame.qysdk.outer.event.IEventListener;
import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.util.Log;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.consts.StatusCodeDef;
import com.qiyuan.gamesdk.core.storage.StorageAgent;
import com.qiyuan.gamesdk.core.storage.events.StorageEvent;
import com.qiyuan.gamesdk.util.ToastUtils;

/**
 * 初始化数据库
 * Created by shui on 4/11/16.
 */
public class InitStorageInterceptor implements InitInterceptor {

    private static final String TAG = "QYSDK:InitStorageInterceptor";

    @Override
    public void intercept(final Chain<InitParams> chain) {

        InitParams data = chain.getData();
        final GameParamInfo gameParamInfo = data.getGameParamInfo();
        final Context mContext = data.getContext();

        final IOperateCallback<String> sdkInitCallback = data.getSdkInitCallback();

        IEventListener<Integer> dbPreListener = new IEventListener<Integer>() {
            @Override
            public void onEvent(String s, Integer code) {
                if (code == StatusCodeDef.SUCCESS) {

                    ApiFacade.preload();
                    ApiFacade.getInstance().setCurrentGameID(peekGameId(gameParamInfo));
                    ApiFacade.getInstance().setSdkKey(gameParamInfo.getSdkKey());
                    ApiFacade.getInstance().setupChannelInfo();

                    chain.proceed(chain.getData());


                } else {
                    if (sdkInitCallback != null) {
                        sdkInitCallback.onResult(StatusCodeDef.FAIL_INIT_SDK_DB_OPEN_ERROR,
                                mContext.getResources().getString(R.string.fail_init_sdk_db_error));
                    }

                }
            }
        };

        EventDispatcherAgent.defaultAgent().addEventListener(InitStorageInterceptor.this,
                StorageEvent.TYPE_ALL_DB_PREPARED, dbPreListener);
        String sdkGameId = peekGameId(gameParamInfo);
        if (TextUtils.isEmpty(sdkGameId)) {
            if (sdkInitCallback != null) {
                sdkInitCallback.onResult(QYCodeDef.ERROR_INIT_INVALID_GAME_ID,
                        mContext.getResources().getString(R.string.error_init_invaild_gameid));
            }
            return;
        }

        StorageAgent.init(mContext.getApplicationContext(), sdkGameId);

    }


    /*
     *使用GameParamInfo传递的gameId
     * @param paramInfo 初始化游戏参数
     * @return 游戏id
     */
    private String peekGameId(GameParamInfo paramInfo) {
        String gameId = paramInfo.getGameId();
        if (TextUtils.isEmpty(gameId)) {
            ToastUtils.showMsg("no available game id.");
        } else {
            Log.d(TAG, "peek game id " + gameId);
        }
        return gameId;
    }

}

