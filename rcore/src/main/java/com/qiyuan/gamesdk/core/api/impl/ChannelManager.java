package com.qiyuan.gamesdk.core.api.impl;

import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.storage.file.PropertiesConfig;
import com.qiyuan.gamesdk.core.storage.file.PropertiesManager;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.api.def.IChannelApi;
import com.qiyuan.gamesdk.core.storage.file.PropertiesConfig;
import com.qiyuan.gamesdk.core.storage.file.PropertiesManager;
import com.qygame.qysdk.outer.util.Log;
import com.qygame.qysdk.outer.util.StringUtils;

/**
 * Created by levyyoung on 15/6/15.
 */
class ChannelManager implements IChannelApi {

    private static final String TAG = "QYSDK:ChannelManager";
    private String currentGameID = "";
    private String currentGameName = "";
    private String SdkKey = "";
    private String channel = "";


    /**
     * 从配置读取渠道信息
     */
    @Override
    public void setupChannelInfo() {
        PropertiesManager pm = PropertiesManager.getInstance();
        channel = pm.getValueFromDefaultConfig(PropertiesConfig.Constant.KEY_CHANNEL);
        Log.e(TAG,"channel: "+channel);

        if (StringUtils.isBlank(channel)) {
            channel = "official";
            Log.e(TAG, "channel is null use default value");
        }
    }

    /**
     * 设置当前游戏Id.
     * 游戏ID由CP传入，initCoreManager时设置。
     *
     * @param gameID 游戏Id
     */
    @Override
    public void setCurrentGameID(String gameID) {
        currentGameID = gameID;
    }

    /**
     * 获取
     *
     * @return 当前游戏ID
     */
    @Override
    public String getCurrentGameID() {
        return currentGameID;
    }

    /**
     * 获取
     * 从当前游戏软件信息中获取
     *
     * @return 当前游戏名称
     */
    @Override
    public String getCurrentGameName() {
        if (!StringUtils.isBlank(currentGameName)) {
            return currentGameName;
        }
        currentGameName = CoreManager.getContext().getApplicationInfo().name;
        return currentGameName;
    }

    @Override
    public String getChannel() {
        Log.d(TAG, "getChannel: ");
        if (ApiFacade.getInstance().isLogin()){
            return ApiFacade.getInstance().getAuth().getChannel();
        }
        return channel;
    }

    @Override
    public String getSdkKey() {
        return SdkKey;
    }

    @Override
    public void setSdkKey(String key) {
        SdkKey = key;
    }
}
