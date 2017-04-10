package com.underconverbg.detectophone;

import android.content.Context;
import android.media.MediaRecorder;
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


    public MyRecorder(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
    }

    public MyRecorder()
    {

    }

    public void start() {
        if (started  == true)
        {
            if (mrecorder != null) {
                mrecorder.stop();
                mrecorder.release();
                mrecorder = null;
            }
        }

        started = true;
        mrecorder = new MediaRecorder();

        File recordPath = new File(
                Environment.getExternalStorageDirectory()
                , "/My record");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.e("recorder", "创建目录");
        }

        String callDir = "呼出";
        if (isCommingNumber) {
            callDir = "呼入";
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
            e.printStackTrace();
        }

        mrecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mrecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mrecorder.setOutputFile(recordName.getAbsolutePath());

        try {
            mrecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                Log.e(TAG , "录音停止");

                String fileName = this.fileName;
                if (fileName != null)
                {
                    Log.e(TAG , "UploadTools 上传");

                    Detect detect = new Detect();
                    detect.setDatetime(this.date);
                    detect.setCallphonenum( getPhoneNumber());
                    detect.setRecordfile(new File(this.fileName));
                    UploadTask task =   new UploadTask(detect);
                    task.uploadFile();
//                    new Thread(task).start();
//                    UploadTools.upload(detect);
                }
                mrecorder = null;
            }
            started = false;
        } catch (IllegalStateException e) {
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
