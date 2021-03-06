package com.qiyuan.gamesdk.core.update;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.base.http.volley.listener.FileDownListener;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.PatchUpdateBean;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.RequestHelper;
import com.qiyuan.gamesdk.core.base.http.volley.listener.FileDownListener;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.model.PatchUpdateBean;
import com.qygame.qysdk.outer.QYSDKSpace;
import com.qygame.qysdk.outer.model.RootDir;
import com.qygame.qysdk.outer.model.VersionDir;
import com.qygame.qysdk.outer.util.Log;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by win on 17-5-8.
 */
public class DefaultUpdateImpl implements IHotfixUpdate {


    public static final String TAG = "QYSDK:UpdateHelper";
    private static final String UPADTE_ZIP_NAME = "update.zip";
    private String sdk_version;
    private String core_version;

    @Override
    public void check() {
        Map<String, String> params = new TreeMap<>();

        RequestHelper.buildParamsWithBaseInfo(params);

        SharedPreferences sharePreferences = QYSDKSpace.getInstance(CoreManager.getContext()).getSharePreferences();
        sdk_version = sharePreferences.getString(QYSDKSpace.KEY_SDK_VERSION, "");
        core_version = sharePreferences.getString(QYSDKSpace.KEY_CUR_VERSION, "");

        params.put("sdkVersion", sdk_version);
        params.put("coreVersion", core_version);


        ApiFacade.getInstance().updateCheck(params, new QyRespListener<PatchUpdateBean>() {

            @Override
            public void onNetSucc(String url, Map<String, String> params, PatchUpdateBean result) {
                Log.d(TAG, "resutl" + result);
                if (result ==null || TextUtils.isEmpty(result.getVersion())){
                    return;
                }

                if (Integer.parseInt(result.getVersion())<=Integer.parseInt(core_version)){
                    Log.e(TAG,"core version is invalid..");
                    return;
                }

                String downurl = result.getDownloadUrl();
                String version = result.getVersion();
                String md5 = result.getSdkMd5();
                downloadFile(downurl, version, md5);

            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                Log.d(TAG, "errno=" + errno + "  errmsg=" + errmsg);
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                Log.d(TAG, "errno=" + errorNo + "  errmsg=" + errmsg);
            }
        });

    }

    private void downloadFile(String downurl, final String version, final String md5) {
        //1.下载apk到指定目录

        RootDir rootPath = RootDir.getInstance(CoreManager.getContext());
        final VersionDir versionDir = new VersionDir(rootPath, version);

        ApiFacade.getInstance().downLoadFile(downurl, versionDir.getApkDir(), UPADTE_ZIP_NAME, md5, new FileDownListener() {
            @Override
            public void onDownLoadSucc(File file) {
                if (file == null) {
                    Log.d(TAG, "down file is null");
                    return;
                }
                new Thread(new ReleaseApkTask(version, file, versionDir)).start();
            }


            @Override
            public void onDownLoadFail(String errmsg) {
                super.onDownLoadFail(errmsg);
                boolean ret = versionDir.deleteVer();
                Log.d(TAG, "download fail delete the download version = " + version + " and the result is " + ret);
            }

        });
    }


}
