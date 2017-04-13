package com.underconverbg.detectophone.upload;

import android.util.Log;

import com.underconverbg.detectophone.ServerCallBack;
import com.underconverbg.detectophone.bean.Detect;

import org.json.JSONException;

import java.io.File;

import okhttp3.Request;

/**
 * Created by user on 2017/4/8.
 */

public  class UploadTools
{
    public final  static String TAG ="UploadTools";

    public static void createUploadThreadAndStart()
    {
        Log.e("UploadTools", "初始化上传服务");
        UploadTaskManager.getInstance();
        UploadTaskManagerThread downloadTaskManagerThread = new UploadTaskManagerThread();
        new Thread(downloadTaskManagerThread).start();
    }

    public static void upload(Detect detect)
    {
        Log.e("UploadTools", "upload");
        UploadTask task =   new UploadTask(detect);
        UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
        uploadTaskMananger.addDownloadTask(task);
    }
}
