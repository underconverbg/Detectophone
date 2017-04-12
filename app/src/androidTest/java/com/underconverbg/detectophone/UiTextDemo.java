package com.underconverbg.detectophone;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.system.SystemSet;
import com.underconverbg.detectophone.upload.UploadTask;
import com.underconverbg.detectophone.upload.UploadTaskManager;
import com.underconverbg.detectophone.upload.UploadTaskManagerThread;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Request;

/**
 * Created by user on 2017/4/12.
 */

@RunWith(AndroidJUnit4.class)
public class UiTextDemo {

    public final  static String TAG ="UiTextDemo";

    @Test
    public  void testUpload()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        SystemSet.getIntance().init(appContext);


        Detect detect = new Detect();
        detect.setDatetime( new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                        .format(new Date(System.currentTimeMillis())));
        detect.setCallphonenum("13750044903");
        String FirstFolder = Environment.getExternalStorageDirectory().getPath();
        detect.setRecordfile(new File(getInnerSDCardPath()+"/luyin20170302113849.3gp"));
        UploadTask task =   new UploadTask(detect);
    }

    @Test
    public  void testUploadPool()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        SystemSet.getIntance().init(appContext);

        //1.new一个线程管理队列
        UploadTaskManager.getInstance();

        //2.new一个线程池，并启动
        UploadTaskManagerThread downloadTaskManagerThread = new UploadTaskManagerThread();
        new Thread(downloadTaskManagerThread).start();

        for (int i = 0 ; i< 100 ; i++) {
            Detect detect = new Detect();
            detect.setDatetime(new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                    .format(new Date(System.currentTimeMillis())));
            detect.setCallphonenum("13750044903");
            String FirstFolder = Environment.getExternalStorageDirectory().getPath();
            detect.setRecordfile(new File(getInnerSDCardPath() + "/luyin20170302113849.3gp"));
            UploadTask task = new UploadTask(detect);
            UploadTaskManager.getInstance().addDownloadTask(task);
        }
    }

    @Test
    public  void testService()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Intent phoneCallStateServiceIntent;
        phoneCallStateServiceIntent = new Intent(appContext,  PhoneCallStateService.class);
        appContext.startService(phoneCallStateServiceIntent);
    }


    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

}
