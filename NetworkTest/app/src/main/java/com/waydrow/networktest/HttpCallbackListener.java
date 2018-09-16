package com.waydrow.networktest;

/**
 * Created by Waydrow on 2017/2/25.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
