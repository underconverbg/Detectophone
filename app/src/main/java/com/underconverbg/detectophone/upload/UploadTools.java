package com.underconverbg.detectophone.upload;

import com.underconverbg.detectophone.bean.Detect;

import java.io.File;

/**
 * Created by user on 2017/4/8.
 */

public  class UploadTools
{

    public static void createUploadThreadAndStart()
    {
        UploadTaskManager.getInstance();
        UploadTaskManagerThread downloadTaskManagerThread = new UploadTaskManagerThread();
        new Thread(downloadTaskManagerThread).start();
    }


    public static void upload(Detect detect)
    {
        UploadTask task =   new UploadTask(detect);
        UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
        uploadTaskMananger.addDownloadTask(task);
    }
}
