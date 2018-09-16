package com.waydrow.servicebestpractice;

/**
 * Created by Waydrow on 2017/2/25.
 */

public interface DownloadListenser {

    void onPorgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
