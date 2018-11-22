package com.qiyuan.gamesdk.core.api.def;

import com.qiyuan.gamesdk.model.AnnouncementInfo;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qiyuan.gamesdk.model.AnnouncementInfo;

import java.util.List;

/**
 *
 * Created by Nekomimi on 2017/6/10.
 */

public interface IAnnouncementApi extends IApiWrapping {

    void requestAnnouncement(int from, IOperateCallback<List<AnnouncementInfo>> callback);
    void requestAnnouncement2(int from, IOperateCallback<List<AnnouncementInfo>> callback);

    List<AnnouncementInfo> getLocalAnnouncement();

}
