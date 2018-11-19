package com.qygame.qysdk.outer;

import android.app.Activity;
import android.content.Context;

import com.qygame.qysdk.outer.model.GameParamInfo;

/**
 * Created by Luoweiqiang 
 * 2015/05/18
 */

public interface ICoreManager   {
    /**
     * @param context
     */
     void init(Context context, boolean debugMode, GameParamInfo gameParamInfo,
               int orientation, IOperateCallback<String> sdkInitCallback, Activity activity);

     String getSession();

     long getUid();

    String getGameId();


    void uninit(IOperateCallback callback);

    void showFloatView();

    void hideFloatView();

}
