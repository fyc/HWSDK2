package com.yiyou.gamesdk.core.ui.dialog.biz;

import android.app.Activity;
import android.content.Context;

import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.event.IDialogParam;
import com.yiyou.gamesdk.core.api.ApiFacade;
import com.yiyou.gamesdk.core.ui.floatview.AnnouncementManager;
import com.yiyou.gamesdk.model.AnnouncementInfo;

import java.util.List;

public class AnnouncementViewController extends BaseAuthViewController {
    public AnnouncementViewController(final Context context, final IDialogParam params) {
        super(context, params);
        ApiFacade.getInstance().requestAnnouncement2(1, new IOperateCallback<List<AnnouncementInfo>>() {
            @Override
            public void onResult(int i, List<AnnouncementInfo> announcementInfos) {
                if (announcementInfos != null && announcementInfos.size() > 0) {
                    for (AnnouncementInfo announcementInfo : announcementInfos) {
                        AnnouncementManager.getInstance().addAnnouncement(announcementInfo);
                    }
                    AnnouncementManager.getInstance().show((Activity) context);
                }
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
