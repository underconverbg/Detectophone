package com.underconverbg.detectophone;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.underconverbg.detectophone.upload.UploadTools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017/3/2.
 */

public class MyRecorder
{
    Context mContext;
    private String phoneNumber;
    private MediaRecorder mrecorder;
    private boolean started = false; //录音机是否已经启动
    private boolean isCommingNumber = false;//是否是来电
    private String TAG = "MyRecorder";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileName ;


    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public MyRecorder(Context context,String phoneNumber)
    {
        mContext = context;
        this.setPhoneNumber(phoneNumber);

        //设置sdcard的路径
        String date = phoneNumber+"Phone" + df.format(new Date());
        //        fileName = c.getFilesDir().getPath();
        String  fileName =  FileUnit.folderCreate(mContext);
        this.fileName += "/"+date+".3gp";

    }

    public MyRecorder(Context context)
    {
        mContext = context;
    }

    public void start()
    {
        Log.e(TAG, "startRecorder fileName:"+fileName);

        started = true;
        mrecorder = new MediaRecorder();

//        mrecorder.setAudioChannels(2);
        mrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);   //获得声音数据源
        mrecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);   // 按3gp格式输出
        mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mrecorder.setAudioSamplingRate(44100);

        if (fileName == null)
        {
            return;
        }

        mrecorder.setOutputFile(fileName);   //输出文件

        mrecorder.setOutputFile(fileName);

        try
        {
            mrecorder.prepare();
        }
        catch (Exception e)
        {
            Log.e(TAG , "录音失败 Exception:"+e.getMessage());
            e.printStackTrace();
        }
        mrecorder.start();
        started = true;
        Log.e(TAG , "录音开始");
    }

    public void stop() {
        try {
            if (mrecorder!=null) {
                mrecorder.stop();
                mrecorder.release();
                mrecorder = null;
            }
            started = false;
        } catch (IllegalStateException e) {
            Log.e(TAG , "停止失败 Exception:"+e.getMessage());
            e.printStackTrace();
        }

        Log.e(TAG , "录音结束");
    }

    public void pause() {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean hasStarted) {
        this.started = hasStarted;
    }

    public boolean isCommingNumber() {
        return isCommingNumber;
    }

    public void setIsCommingNumber(boolean isCommingNumber) {
        this.isCommingNumber = isCommingNumber;
    }


}
