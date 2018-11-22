package com.qiyuan.gamesdk.core.api.def;

import android.support.annotation.NonNull;

import com.qiyuan.gamesdk.core.base.http.volley.listener.FileDownListener;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.PatchUpdateBean;
import com.qiyuan.gamesdk.core.base.http.volley.listener.FileDownListener;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.GameUpdateInfo;
import com.qiyuan.gamesdk.model.PatchUpdateBean;

import java.io.File;
import java.util.Map;

public interface IUpgradeApi extends IApiWrapping{

	/**
     * 检查游戏更新
     * @param cpId 游戏厂商id  
     * @param gameId 游戏id
	 * @param lisenter 调用方回调。成功返回GameUpdateInfo ，失败返回错误码和错误信息
	 */
	void upgradeRequest(String cpId, String gameId, String versionName, String versionCode, QyRespListener<GameUpdateInfo> lisenter) ;


	/**
	 * 检查热更
	 *
	 * @param params   params
	 * @param listener callback
	 */
	void updateCheck(@NonNull Map<String, String> params, QyRespListener<PatchUpdateBean> listener);

	/**
	 * @param url      url
	 * @param saveFile 保存文件
	 * @param listener callback
	 */
	void downLoadFile(String url, File saveFile, String fileName, String md5, FileDownListener listener);

}
