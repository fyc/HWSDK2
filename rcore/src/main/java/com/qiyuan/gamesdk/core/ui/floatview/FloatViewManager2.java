package com.qiyuan.gamesdk.core.ui.floatview;

import android.os.Handler;
import android.os.Looper;

import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qygame.qysdk.outer.util.ResourceHelper;

public class FloatViewManager2 {

    private static FloatViewManager2 instance;
    private FloatViewController2 floatViewController2;

    private FloatViewManager2() {
        floatViewController2 = new FloatViewController2(ResourceHelper.getContextHolder().get());
    }

    public static synchronized FloatViewManager2 getInstance() {
        if (instance == null) {
            instance = new FloatViewManager2();
        }
        return instance;
    }


    public synchronized void destory() {
//        floatViewController = null;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (floatViewController2!=null) {
                    floatViewController2.hide();
                    floatViewController2 = null;
                    instance =null;
                }
            }
        });
    }


    public synchronized void show() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (floatViewController2 != null && ApiFacade.getInstance().getMainUid() != 0) {
                    floatViewController2.show();
                }
            }
        });
    }

    public synchronized void hide() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (floatViewController2!=null) {
                    floatViewController2.hide();
                }
            }
        });
    }

}
