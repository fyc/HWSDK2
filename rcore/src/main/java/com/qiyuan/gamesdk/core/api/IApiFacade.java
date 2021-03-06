package com.qiyuan.gamesdk.core.api;

import com.qiyuan.gamesdk.core.api.def.IAccountHistoryApi;
import com.qiyuan.gamesdk.core.api.def.IAnnouncementApi;
import com.qiyuan.gamesdk.core.api.def.IAuthApi;
import com.qiyuan.gamesdk.core.api.def.IChannelApi;
import com.qiyuan.gamesdk.core.api.def.IChildrenAccountHistoryApi;
import com.qiyuan.gamesdk.core.api.def.IPaymentApi;
import com.qiyuan.gamesdk.core.api.def.IReportApi;
import com.qiyuan.gamesdk.core.api.def.ISecurityApi;
import com.qiyuan.gamesdk.core.api.def.IUpgradeApi;

/**
 * Created by levyyoung on 15/6/11.
 */
public interface IApiFacade extends IChannelApi, ISecurityApi, IUpgradeApi, IReportApi, IAuthApi, IChildrenAccountHistoryApi,
         IAccountHistoryApi, IPaymentApi, IAnnouncementApi{
}
