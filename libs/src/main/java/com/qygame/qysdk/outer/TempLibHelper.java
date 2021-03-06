package com.qygame.qysdk.outer;

import com.qygame.qysdk.outer.model.VersionDir;
import com.qygame.qysdk.outer.util.FileUtils;
import com.qygame.qysdk.outer.util.Log;

/**
 * Created by shui on 15-9-8.
 */
public class TempLibHelper {

    private static final String TAG = "QYSDK: TempLibHelper";
    private static TempLibHelper instance;
    private String lib_temp = "lib-temp";


    private TempLibHelper() {
        lib_temp = String.format(lib_temp + "%s", System.currentTimeMillis() / 1000 + "");
        Log.d(TAG, "getLibTemp" + lib_temp);



    }

    private static void copyTempSo(VersionDir versionDir){
        boolean ret = FileUtils.copyDir(versionDir.getLibDir(), versionDir.getLibTemp());
        Log.d(TAG, "copy temp" + ret);

    }


    public String getLib_temp() {
        return lib_temp;
    }

    public static TempLibHelper getInstance(VersionDir versionDir) {
        if (instance == null) {
            instance = new TempLibHelper();
            copyTempSo(versionDir);
        }
        return instance;
    }

    public void uninit() {
        lib_temp = "";
        instance = null;
    }
}
