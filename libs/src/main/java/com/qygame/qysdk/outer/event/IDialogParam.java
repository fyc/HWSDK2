package com.qygame.qysdk.outer.event;

import android.content.Context;

import com.qygame.qysdk.outer.IOperateCallback;

/**
 * Created by Orange on 15/6/15.
 */
public interface IDialogParam {
    Context getActivityContext();

    IOperateCallback<String> getAppCallback();

}
