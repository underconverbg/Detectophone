package com.underconverbg.detectophone.upload;

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


    public static void upload(String fileLoad)
    {
        File file = new File(fileLoad);
        file.getName();
        UploadTask task =   new UploadTask(file);
        UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
        uploadTaskMananger.addDownloadTask(task);
    }
}
