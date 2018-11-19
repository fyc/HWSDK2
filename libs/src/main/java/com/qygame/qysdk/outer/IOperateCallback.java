package com.qygame.qysdk.outer;

/**
 * Created by levyyoung on 15/4/30.
 */

/**
 *
 * @param <T>
 */
public interface IOperateCallback<T> {

    public void onResult(int code, T resultData);

}

