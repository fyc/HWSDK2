package com.mobilegamebar.rsdk.outer.model;


/**
 * Created by Win on 17/4/25.
 */
public class GameParamInfo {

    private String    gameId = "";
    private String SdkKey = "";

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getSdkKey() {
        return SdkKey;
    }

    public void setSdkKey(String sdkKey) {
        SdkKey = sdkKey;
    }

    @Override
    public String toString() {
        return "GameParamInfo{" +
                "gameId=" + gameId +
                ", SdkKey='" + SdkKey + '\'' +
                '}';
    }
}
