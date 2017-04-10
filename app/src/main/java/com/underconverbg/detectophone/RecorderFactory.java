package com.underconverbg.detectophone;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2017/3/1.
 */

public class RecorderFactory
{
    public static int  TYPE_VOICE_CALL = 0;
    public static int TYPE_MIC = 1;


    public static final String LOG_TAG = "RecorderFactory";

    // 定义一个私有的构造方法
    private RecorderFactory()
    {
    }

    // 将自身的实例对象设置为一个属性,并加上Static和final修饰符
    private static final RecorderFactory instance = new RecorderFactory();

    // 静态方法返回该类的实例
    public static RecorderFactory getInstancei()
    {
        return instance;
    }

    private MediaRecorder mRecorder = null;

    //语音文件保存路径
    private String fileName = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public String startRecorder(String title,Context c,int type)
    {
        mRecorder = new MediaRecorder();
        //设置sdcard的路径
        String date = title + df.format(new Date());

//        fileName = c.getFilesDir().getPath();
        fileName =  FileUnit.folderCreate(c);
        fileName += "/"+date+".3gp";


        Log.e(LOG_TAG, "startRecorder fileName:"+fileName);

        if (TYPE_VOICE_CALL == type)
        {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        }
        else
        {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }

        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try
        {
            mRecorder.prepare();
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "prepare() failed");
            return  null;
        }
        mRecorder.start();

        return fileName;
    }

    public void stopRecorder()
    {

        if (mRecorder == null)
        {
            Log.e(LOG_TAG, "mRecorder() null");
            return;
        }
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            Log.e(LOG_TAG, "stop() success");

        }catch (Exception e)
        {
            Log.e(LOG_TAG, "stop() failed");
        }
    }
}
