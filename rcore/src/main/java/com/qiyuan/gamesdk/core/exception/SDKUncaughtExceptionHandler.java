package com.qiyuan.gamesdk.core.exception;


import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.util.PhoneUtils;
import com.qiyuan.gamesdk.util.TimeUtils;
import com.qiyuan.gamesdk.core.CoreManager;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qygame.qysdk.outer.util.StorageConfig;
import com.qiyuan.gamesdk.util.PhoneUtils;
import com.qiyuan.gamesdk.util.TimeUtils;

import java.io.File;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

/**
 * Created by chenshuide on 15/7/20.
 */
public class SDKUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private static final String TAG = "QYSDK:SDKUncaughtExceptionHandler ";
    private UncaughtExceptionHandler defaultHandler;


    public SDKUncaughtExceptionHandler() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        writeException2File(ex);

        if (defaultHandler != null)
            defaultHandler.uncaughtException(thread, ex);
        else
            System.exit(0);

    }

    private void writeException2File(Throwable ex) {

        try {

            String filename = "error-" + TimeUtils.format(new Date()) + ".txt";
            File errLogFile = new File(StorageConfig.getGameLogDirPath(ApiFacade.getInstance().getCurrentGameID()), filename);

            if (!errLogFile.exists()) {
                errLogFile.createNewFile();
            }

            String deviceInfo = PhoneUtils.collectDeviceInfo(CoreManager.getContext());
            PrintWriter pw = new PrintWriter(errLogFile);
            ex.printStackTrace(pw);

            pw.write("\n");
            pw.write(deviceInfo);
            pw.flush();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
