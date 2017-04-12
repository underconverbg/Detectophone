package com.underconverbg.detectophone;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.upload.UploadTask;
import com.underconverbg.detectophone.upload.UploadTools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017/3/2.
 */

public class MyRecorder
{
    private String fileName;
    private String date;
    private String phoneNumber;

    private MediaRecorder mrecorder;
    private boolean started = false; //录音机是否已经启动
    private boolean isCommingNumber = false;//是否是来电
    private String TAG = "Recorder";


    public void start()
    {
        if (started  == true)
        {
            if (mrecorder != null) {
                mrecorder.stop();
                mrecorder.release();
                mrecorder = null;
            }
        }
        mrecorder = new MediaRecorder();

        File recordPath = new File(
                Environment.getExternalStorageDirectory()
                , "/detectophone");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.e("recorder", "创建目录");
        }
        else if( !recordPath.isDirectory() && recordPath.canWrite() ){
            recordPath.delete();
            recordPath.mkdirs();
        }
        else{
            //you can't access there with write permission.
            //Try other way.
        }

        String callDir = "out";
        if (isCommingNumber)
        {
            callDir = "in";
        }


        date =  new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                .format(new Date(System.currentTimeMillis()));

        fileName = callDir + "-" + phoneNumber + "-"
                + date + ".mp3";//实际是3gp
        File recordName = new File(recordPath, fileName);

        try {
            recordName.createNewFile();
            Log.e("recorder", "创建文件" + recordName.getName());
        } catch (IOException e) {
            Log.e("recorder错误 IOException", "错误" );

            if (e !=  null){
                e.printStackTrace();
                Log.e("recorder", "错误" + e.getMessage());
            }

        }


        mrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);//从麦克风采集声音
        mrecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); //内容输出格式
        mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码方式
        mrecorder.setOutputFile(recordName.getAbsolutePath());

        try {
            mrecorder.prepare();
            mrecorder.start();
            setStarted(true);
            Log.e(TAG , "录音开始");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误IllegalStateException" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误IOException" + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            setStarted(false);
            Log.e("recorder", "录音开始错误Exception" + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (mrecorder!=null)
            {
                mrecorder.stop();
                mrecorder.release();
                setStarted(false);
                Log.e(TAG , "录音停止");

                String fileName = this.fileName;

                if (fileName != null)
                {
                    Log.e(TAG , "UploadTools 上传");

                    Detect detect = new Detect();
                    detect.setDatetime(this.date);
                    detect.setCallphonenum( getPhoneNumber());
                    detect.setRecordfile(new File(this.fileName));

                    Log.e(TAG , detect.toString());
                    UploadTools.upload(detect);
                }
                mrecorder = null;
            }
        } catch (IllegalStateException e) {
            Log.e(TAG , "录音错误结束");
            e.printStackTrace();
        }

        Log.e(TAG , "录音结束");
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
