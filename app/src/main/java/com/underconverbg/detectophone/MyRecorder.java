package com.underconverbg.detectophone;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.upload.UploadTools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.media.MediaRecorder.AudioSource.DEFAULT;
import static android.media.MediaRecorder.AudioSource.MIC;
import static android.media.MediaRecorder.AudioSource.VOICE_CALL;
import static android.media.MediaRecorder.AudioSource.VOICE_UPLINK;

/**
 * Created by user on 2017/3/2.
 */

public class MyRecorder
{
    private String filePath;

    private String date;
    private String phoneNumber;

    private MediaRecorder mrecorder;
    private boolean isCommingNumber = false;//是否是来电
    private String TAG = "Recorder";


    public synchronized void start()
    {
        File recordPath = new File(Environment.getExternalStorageDirectory()
                , "/mydetectophone");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.e("recorder", "创建目录");
        }
        else if( !recordPath.isDirectory() && recordPath.canWrite() )
        {
            Log.e("recorder", "删除创建目录");
            recordPath.delete();
            recordPath.mkdirs();
        }
        else
        {
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

        String accName = callDir + "-" + phoneNumber + "-"
                + date + ".3gp";//实际是3gp
        File recordFile = new File(recordPath, accName);

        mrecorder = new MediaRecorder();

        try {
            mrecorder.stop();
            mrecorder.release();
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e("recorder", "录音开始停止错误IllegalStateException" + e.getMessage());
        }

        filePath = recordFile.getAbsolutePath();

        mrecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mrecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置所录制的声音的编码位率。
        mrecorder.setAudioEncodingBitRate(16);
        //设置所录制的声音的采样率。
        mrecorder.setAudioSamplingRate(44100);
        mrecorder.setOutputFile(filePath);


        try {
            mrecorder.prepare();
            mrecorder.start();
            Log.e(TAG , "录音开始");
            Log.e(TAG , "录音并设置开始");

        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误IllegalStateException" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误IOException" + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误Exception" + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (mrecorder!=null)
            {
                mrecorder.stop();
                mrecorder.release();
                setIsCommingNumber(false);

                Log.e(TAG , "录音停止");

                if (filePath != null)
                {
                    Log.e(TAG , "UploadTools 上传");

                    Detect detect = new Detect();
                    detect.setDatetime(this.date);
                    detect.setCallphonenum( getPhoneNumber());
                    detect.setRecordfile(new File(filePath));

                    Log.e(TAG , detect.toString());
                    UploadTools.upload(detect);
                }
                mrecorder = null;
            }
        } catch (IllegalStateException e)
        {
            Log.e(TAG , "录音错误结束");
            mrecorder = null;
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

    public synchronized boolean isCommingNumber() {
        return isCommingNumber;
    }

    public synchronized void setIsCommingNumber(boolean isCommingNumber) {
        this.isCommingNumber = isCommingNumber;
    }
}
