package com.qiyuan.gamesdk.core.storage.events;

import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;
import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;


public class NativeTitleBarUpdateEvent {

    public static final String TYPE_ON_NATIVE_TITLE_BAR_UPDATE = "com.qiyuan.gamesdk.events.type.nativetitlebarupdate";

    public static class NativeTitleBarUpdateEventParam {
        public NativeTitleBarUpdateInfo mNativeTitleBarUpdateInfo ;

        public NativeTitleBarUpdateEventParam() {
        }

        public NativeTitleBarUpdateEventParam(NativeTitleBarUpdateInfo nativeTitleBarUpdateInfo) {
            this.mNativeTitleBarUpdateInfo = nativeTitleBarUpdateInfo;
        }

        @Override
        public String toString() {
            return "NativeTitleBarUpdateEvent{" +
                    "NativeTitleBarUpdateInfo=" +mNativeTitleBarUpdateInfo.toString() +
                    '}';
        }
    }
}
